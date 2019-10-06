package com.example.blink.activity

import android.Manifest
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import com.example.blink.App
import com.example.blink.R
import com.example.blink.UploadStatusCode
import com.example.blink.fragments.SharingMapFragment
import com.example.blink.fragments.ViewPagerAdapter
import com.example.blink.utils.BlinkService
import com.google.android.material.tabs.TabLayout
import java.io.File


class SharingActivity : AppCompatActivity() {

    private lateinit var viewpager: ViewPager
    private lateinit var tabs: TabLayout
    private lateinit var uploadedUuid: String

    fun getUploadedUuid(): String {
        return this.uploadedUuid
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sharing)

        tabs = findViewById(R.id.tabs)
        viewpager = findViewById(R.id.viewpager)

        setupViewPager()

        this.sendFileToServer(intent)
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(getSupportFragmentManager())

        var firstFragment: SharingMapFragment = SharingMapFragment()
//        var secondFragment: ListFragment = ListFragment.newInstance("Second Fragment")

        adapter.addFragment(firstFragment, "Search")
//        adapter.addFragment(secondFragment, "List")

        viewpager!!.adapter = adapter

        tabs!!.setupWithViewPager(viewpager)
    }

    private fun sendFileToServer(intent: Intent) {
        val permission = ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }

        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            Log.d("TAG", it.toString())
            SendFileToServer().execute(it)
        }
    }

    inner class SendFileToServer : AsyncTask<Uri, Void, String>() {
        override fun doInBackground(vararg params: Uri?): String? {
            val uri = params[0]!!
            val blinkService: BlinkService = BlinkService.getInstance()
            val path = getUriRealPathAboveKitkat(applicationContext, uri)
            val splitted = path!!.split("/")
            val response = blinkService.uploadFileRequest(App.prefs.myUserName, splitted[splitted.size - 1])

            if (response == null) {
                return null
            }

            val sendFileResult = blinkService.uploadFile(File(getUriRealPathAboveKitkat(applicationContext, uri)), response)
            val uploadResultCode = sendFileResult.code

            Log.d("dasdas", "${uploadResultCode}")

//            if (uploadResultCode != UploadStatusCode.OK) {
//                return null
//            }

            Log.d("dasdas", "${response}")
            App.prefs.myUuid = response
            uploadedUuid = response

            return response
        }

        /* Get uri related content real local file path. */
        private fun getUriRealPath(ctx: Context, uri: Uri): String? {
            var ret: String? = ""
            if (isAboveKitKat()) {
                // Android OS above sdk version 19.
                ret = getUriRealPathAboveKitkat(ctx, uri)
            } else {
                // Android OS below sdk version 19
                ret = getImageRealPath(contentResolver, uri, null)
            }
            return ret
        }

        private fun getUriRealPathAboveKitkat(ctx: Context?, uri: Uri?): String? {
            var ret: String? = ""
            if (ctx != null && uri != null) {
                if (isContentUri(uri)) {
                    if (isGooglePhotoDoc(uri.authority)) {
                        ret = uri.lastPathSegment
                    } else {
                        ret = getImageRealPath(contentResolver, uri, null)
                    }
                } else if (isFileUri(uri)) {
                    ret = uri.path
                } else if (isDocumentUri(ctx, uri)) {
                    // Get uri related document id.
                    val documentId = DocumentsContract.getDocumentId(uri)
                    // Get uri authority.
                    val uriAuthority = uri.authority
                    if (isMediaDoc(uriAuthority)) {
                        val idArr = documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (idArr.size == 2) {
                            // First item is document type.
                            val docType = idArr[0]
                            // Second item is document real id.
                            val realDocId = idArr[1]
                            // Get content uri by document type.
                            var mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            if ("image" == docType) {
                                mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            } else if ("video" == docType) {
                                mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            } else if ("audio" == docType) {
                                mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                            }
                            // Get where clause with real document id.
                            val whereClause = MediaStore.Images.Media._ID + " = " + realDocId
                            ret = getImageRealPath(contentResolver, mediaContentUri, whereClause)
                        }
                    } else if (isDownloadDoc(uriAuthority)) {
                        // Build download uri.
                        val downloadUri = Uri.parse("content://downloads/public_downloads")
                        // Append download document id at uri end.
                        val downloadUriAppendId =
                            ContentUris.withAppendedId(downloadUri, java.lang.Long.valueOf(documentId))
                        ret = getImageRealPath(contentResolver, downloadUriAppendId, null)
                    } else if (isExternalStoreDoc(uriAuthority)) {
                        val idArr = documentId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        if (idArr.size == 2) {
                            val type = idArr[0]
                            val realDocId = idArr[1]
                            if ("primary".equals(type, ignoreCase = true)) {
                                ret = Environment.getExternalStorageDirectory().toString() + "/" + realDocId
                            }
                        }
                    }
                }
            }
            return ret
        }

        /* Check whether current android os version is bigger than kitkat or not. */
        private fun isAboveKitKat(): Boolean {
            var ret = false
            ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
            return ret
        }

        /* Check whether this uri represent a document or not. */
        private fun isDocumentUri(ctx: Context?, uri: Uri?): Boolean {
            var ret = false
            if (ctx != null && uri != null) {
                ret = DocumentsContract.isDocumentUri(ctx, uri)
            }
            return ret
        }

        /* Check whether this uri is a content uri or not.
         * content uri like content://media/external/images/media/1302716
         **/
        private fun isContentUri(uri: Uri?): Boolean {
            var ret = false
            if (uri != null) {
                val uriSchema = uri.scheme
                if ("content".equals(uriSchema!!, ignoreCase = true)) {
                    ret = true
                }
            }
            return ret
        }

        /* Check whether this uri is a file uri or not.
         * file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
         **/
        private fun isFileUri(uri: Uri?): Boolean {
            var ret = false
            if (uri != null) {
                val uriSchema = uri.scheme
                if ("file".equals(uriSchema!!, ignoreCase = true)) {
                    ret = true
                }
            }
            return ret
        }

        /* Check whether this document is provided by ExternalStorageProvider. */
        private fun isExternalStoreDoc(uriAuthority: String?): Boolean {
            var ret = false
            if ("com.android.externalstorage.documents" == uriAuthority) {
                ret = true
            }
            return ret
        }

        /* Check whether this document is provided by DownloadsProvider. */
        private fun isDownloadDoc(uriAuthority: String?): Boolean {
            var ret = false
            if ("com.android.providers.downloads.documents" == uriAuthority) {
                ret = true
            }
            return ret
        }

        /* Check whether this document is provided by MediaProvider. */
        private fun isMediaDoc(uriAuthority: String?): Boolean {
            var ret = false
            if ("com.android.providers.media.documents" == uriAuthority) {
                ret = true
            }
            return ret
        }

        /* Check whether this document is provided by google photos. */
        private fun isGooglePhotoDoc(uriAuthority: String?): Boolean {
            var ret = false
            if ("com.google.android.apps.photos.content" == uriAuthority) {
                ret = true
            }
            return ret
        }

        /* Return uri represented document file real local path.*/
        private fun getImageRealPath(contentResolver: ContentResolver, uri: Uri, whereClause: String?): String {
            var ret = ""
            // Query the uri with condition.
            val cursor = contentResolver.query(uri, null, whereClause, null, null)
            if (cursor != null) {
                val moveToFirst = cursor.moveToFirst()
                if (moveToFirst) {
                    // Get columns name by uri type.
                    var columnName = MediaStore.Images.Media.DATA
                    if (uri === MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                        columnName = MediaStore.Images.Media.DATA
                    } else if (uri === MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
                        columnName = MediaStore.Audio.Media.DATA
                    } else if (uri === MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                        columnName = MediaStore.Video.Media.DATA
                    }
                    // Get column index.
                    val imageColumnIndex = cursor.getColumnIndex(columnName)
                    // Get column value which is the uri related file local path.
                    ret = cursor.getString(imageColumnIndex)
                }
            }
            return ret
        }
    }
}
