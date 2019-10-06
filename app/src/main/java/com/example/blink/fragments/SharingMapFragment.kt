package com.example.blink.fragments

import android.content.pm.PackageManager
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.blink.Client
import com.example.blink.R
import com.example.blink.activity.SharingActivity
import com.example.blink.utils.BlinkService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class SharingMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mapView: MapView

    private lateinit var lastLocation: Location

    private lateinit var mHandler: Handler

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                SharingMapFragment.LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(requireActivity()) { location ->

            Log.d("gRPC", "location")

            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14.0f))



                object : Runnable{
                    override fun run() {
                        mHandler.postDelayed(this, 10000)
                        //5 seconds time interval
                        GetClientByLocation().execute(location.latitude.toFloat(), location.longitude.toFloat())
                    }
                }


            }
        }
    }

    fun synchronizeMap() {
        mapView!!.getMapAsync(this);
    }

    fun addMarker(clients: ArrayList<Client>) {
        Log.d("gRPC","${clients.size}")
        for (client in clients) {
            var location = client.location
            var lan = location.latitude.toDouble()
            var lon = location.longitude.toDouble()

            map.addMarker(
                MarkerOptions()
                    .position(LatLng(lan, lon)).title(client.nickname)
            )
        }
        synchronizeMap()
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        var activity: SharingActivity = activity as SharingActivity
        var uuid = activity.getUploadedUuid()
        Log.d("gRPC", uuid)
        return false
    }

    inner class GetClientByLocation : AsyncTask<Float, Void, ArrayList <Client>>() {
        override fun doInBackground(vararg params: Float?): ArrayList<Client>? {
            var service: BlinkService = BlinkService.getInstance()
            var response = service.getClientsByLocation(params[0]!!, params[1]!!)

            Log.d("gRPC", "${response}")

            return response
        }

        override fun onPostExecute(result: ArrayList<Client>?) {
            addMarker(result!!)
        }
    }
}