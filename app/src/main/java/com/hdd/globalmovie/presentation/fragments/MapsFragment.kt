package com.hdd.globalmovie.presentation.fragments

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.hdd.globalmovie.R
import com.hdd.globalmovie.data.models.LatitudeLongitude

class MapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private var locationList = ArrayList<LatitudeLongitude>()
    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap
        val store1 = LatitudeLongitude(27.69486292102715, 84.4230918354373, "Store 1, Chitwan")
        val store2 = LatitudeLongitude(27.691965134338325,   84.42483839591884, "Store 2, Hakimchowk")
        locationList = arrayListOf<LatitudeLongitude>(store1, store2)
        for (location in locationList) {
            mMap.addMarker(
                MarkerOptions().position(LatLng(location.latitude, location.longitude))
                    .title(location.markerName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            )
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(27.69486292102715, 84.4230918354373), 16F));
        mMap.uiSettings.isZoomControlsEnabled = true

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}