package com.mptsix.todaydiary.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.ActivityMapBinding
import com.mptsix.todaydiary.view.fragment.EditDiaryFragment

class MapActivity : AppCompatActivity() {
    lateinit var binding: ActivityMapBinding
    lateinit var googleMap: GoogleMap
    var loc = LatLng(37.5642135, 127.0016985)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initMap()
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))
            googleMap.setMinZoomPreference(6.0f)
            googleMap.setMaxZoomPreference(18.0f)
            val option = MarkerOptions()
            option.position(loc)
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            val mk = googleMap.addMarker(option)
            mk.showInfoWindow()
            initMapListener()
        }
    }

    private fun initMapListener() {
        lateinit var location :LatLng
        googleMap.setOnMapClickListener {
            googleMap.clear()
            val option = MarkerOptions()
            option.position(it)
            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            googleMap.addMarker(option)
            location = it
            var intent = Intent(location.toString())
            //Toast.makeText(this, location.toString(),Toast.LENGTH_SHORT).show() --> 여기까진 잘됨
            setResult(0, intent)
            finish()
        }
    }
}