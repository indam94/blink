package com.example.blink.utils

import android.os.AsyncTask
import android.os.Environment
import com.google.common.io.Flushables.flush
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import com.example.blink.App
import io.grpc.internal.ReadableBuffers.openStream
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.net.URL
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import androidx.core.content.ContextCompat.getSystemService

class DownloadFile {
    companion object {
        fun downloadFromUrl(link: String, context: Context): String? {
            val splitted = link.split("?filename=")
            val url = "http://" + App.prefs.myIp + ":3000" + splitted[0]
            val filename = splitted[1]

            try {
                val uri = Uri.parse(link)

                val request = DownloadManager.Request(uri)
                request.setTitle("Blink File Transfer")
                request.setDescription("Downloading")
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setVisibleInDownloadsUi(false)
                request.setDestinationUri(Uri.parse("file://" + getExternalStorageDirectory().toString() + "/Download/" + filename))

//                val url = URL(url)
//                val conection = url.openConnection()
//                conection.connect()
//
//                // download the file
//                val input = BufferedInputStream(
//                    url.openStream(),
//                    8192
//                )
//
//                // Output stream
//                val output = FileOutputStream(
//                    getExternalStorageDirectory().toString() + "/Download/" + filename
//                )
//
//                Log.d("DownloadFile", "File will be settled on " + getExternalStorageDirectory().toString() + "/Download/" + filename)
//
//                val data = ByteArray(1024)
//                var fullCount: Int = 0
//                var offset = 0
//
//                while (true) {
//                    fullCount++
//                    var count = input.read(data)
//                    if (count <= 0) break
//                    output.write(data, offset, count)
//                    offset += count
//
//                    if (fullCount % 100 == 0) {
//                        Log.d("DownloadFile", fullCount.toString() + " chunks downloaded");
//                    }
//                }
//
//                output.flush()
//
//                output.close()
//                input.close()
//
//                Log.d("DownloadFile", "Download was successfully finished")
//
//                return getExternalStorageDirectory().toString() + "/Download/" + filename
            } catch (e: Exception) {
                Log.e("Error: ", e.message)
            }
            return null;
        }
    }
}