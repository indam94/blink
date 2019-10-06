package com.example.blink.activity

import android.app.DownloadManager
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.blink.App
import com.example.blink.R
import com.example.blink.ReceiveRequest
import com.example.blink.fragments.SpotMapFragment
import com.example.blink.fragments.ViewPagerAdapter
import com.example.blink.utils.BlinkService
import com.example.blink.utils.DownloadFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout

class SpotActivity : AppCompatActivity() {

    private lateinit var viewpager: ViewPager
    private lateinit var tabs: TabLayout

    private lateinit var lastlocation: Location

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var check = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spot)

        initViews()

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastlocation = location

                if (!check) {
                    SetReceiverStream().execute()
                    check = true
                }
            }
        }

        setupViewPager()
    }

    private fun initViews() {
        tabs = findViewById(R.id.tabs)
        viewpager = findViewById(R.id.viewpager)
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(getSupportFragmentManager())

        var firstFragmet = SpotMapFragment()
        //var secondFragmet: ListFragment = ListFragment.newInstance("Second Fragment")

        adapter.addFragment(firstFragmet, "Search")
        //adapter.addFragment(secondFragmet, "List")

        viewpager!!.adapter = adapter

        tabs!!.setupWithViewPager(viewpager)
    }

    private fun askReceiveRequest(request: ReceiveRequest) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Receiving File")
        dialog.setMessage("${request.nickname} wants to share ${request.filename} with you.")
        dialog.setPositiveButton("OK", { d: DialogInterface, which: Int ->
            ResponseGrant().execute(request)
            d.dismiss()
        })
        dialog.setNegativeButton("Deny", { d: DialogInterface, which: Int ->
            Toast.makeText(this, "File share was canceled.", Toast.LENGTH_LONG).show()
            ResponseDenial().execute(request)
            d.dismiss()
        })
        dialog.show()
    }

    inner class SetReceiverStream : AsyncTask<Void, Boolean, ReceiveRequest>() {

        override fun doInBackground(vararg params: Void): ReceiveRequest? {
            var service = BlinkService.getInstance()

            try {
                Thread.sleep(1000)
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }

            var response = service.setReceiverStream(
                App.prefs.myUserName
                , lastlocation.latitude.toFloat(), lastlocation.longitude.toFloat()
            )

            return response
        }

        override fun onPostExecute(result: ReceiveRequest?) {
            if (result == null) {
                SetReceiverStream().execute()
                return
            }

            askReceiveRequest(result!!)
        }
    }

    inner class ResponseGrant : AsyncTask<ReceiveRequest, Void, String>() {
        override fun doInBackground(vararg params: ReceiveRequest?): String? {
            val request = params[0]!!
            val service = BlinkService.getInstance()
            val link = service.respondGrant(request.nickname, request.receiverNickname, request.filename, request.uuid)

            val splitted = link.split("?filename=")
            val url = "http://" + App.prefs.myIp + ":3000" + splitted[0]
            val filename = splitted[1]

            try {
                val downloadmanager = ContextCompat.getSystemService<DownloadManager>(applicationContext, DownloadManager::class.java)
                val uri = Uri.parse(url)

                val request = DownloadManager.Request(uri)
                request.setTitle("Blink File Transfer")
                request.setDescription("Downloading")
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                request.setVisibleInDownloadsUi(false)
                request.setDestinationUri(Uri.parse("file://" + Environment.getExternalStorageDirectory().toString() + "/Download/" + filename))

                downloadmanager!!.enqueue(request)
            } catch (e: Exception) {
                Log.e("Error: ", e.message)
            }
            return Environment.getExternalStorageDirectory().toString() + "/Download/" + filename
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result == null) {
                Toast.makeText(applicationContext, "Download failed. Try again.", Toast.LENGTH_LONG).show()
                return
            }

            Toast.makeText(applicationContext, "File was successfully downloaded on:\n" + result, Toast.LENGTH_LONG).show()

            SetReceiverStream().execute()
        }
    }

    inner class ResponseDenial : AsyncTask<ReceiveRequest, Void, Boolean>() {
        override fun doInBackground(vararg params: ReceiveRequest?): Boolean {
            val request = params[0]!!
            val service = BlinkService.getInstance()
            val result = service.respondDenial(request.nickname, request.receiverNickname, request.filename, request.uuid)

            SetReceiverStream().execute()
            return result
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)

            Log.d("adadas",result.toString())
        }
    }


}
