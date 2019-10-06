package com.example.blink

import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private var mapView: MapView? = null
    private lateinit var mapMarkers: ArrayList<Marker>

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onMarkerClick(p0: Marker?) = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var file = activity!!.intent.data

        val regex = """(.+)/(.+)\.(.+)""".toRegex()
        val matchResult = regex.matchEntire(file.path)

        if (matchResult != null) {
            val (directory, fileName, extension) = matchResult.destructured
            //println("dir: $directory | fileName: $fileName | extension: $extension")

            var async = UploadFileRequest()
            async.execute(App.prefs.myUserName, fileName+'.'+extension)
        }



    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return activity!!.layoutInflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = view.findViewById(R.id.map) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.onResume()
        mapView!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = false
        map.setOnMarkerClickListener(this)

        setUpMap()
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                MapFragment.LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        map.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f))

                //if SharingActivity else MainActivity
                //if(){}
                var async = GetClientByLocation()
                async.execute(location.latitude as Float, location.longitude as Float)
            }
        }
    }

    public fun addMarker(clients: ArrayList<Client>){
        for(client in clients){
            var location = client.location
            var lan = location.latitude as Double
            var lon = location.longitude as Double

            map.addMarker(MarkerOptions()
                .position(LatLng(lan, lon)).title(client.nickname))
        }
        mapView!!.getMapAsync(this)
    }

    inner class UploadFileRequest: AsyncTask<String, String, Void>(){
        override fun doInBackground(vararg params: String?): Void? {

            var service: BlinkService = BlinkService.getInstance()
            var response = service.uploadFileRequest(params[0]!!, params[1]!!)

            Log.d("gRPC", "${response}")

            //File => uuid => uploadFile
            if(response != ""){
                var async = UploadFile()
                async.execute()
            }

            publishProgress()
            return null
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)
        }
    }

    inner class GetClientByLocation: AsyncTask<Float, Client, Void?>(){
        override fun doInBackground(vararg params: Float?): Void? {

            var service: BlinkService = BlinkService.getInstance()
            var response = service.getClientsByLocation(params[0]!!, params[1]!!)

            Log.d("gRPC", "${response}")

            addMarker(response)

            return null
        }

        override fun onProgressUpdate(vararg values: Client?) {
            super.onProgressUpdate(*values)
        }
    }

    inner class UploadFile: AsyncTask<String, UploadFileResp, Void?>(){
        override fun doInBackground(vararg params: String?): Void? {



            return null
        }

        override fun onProgressUpdate(vararg values: UploadFileResp?) {
            super.onProgressUpdate(*values)

        }
    }
}