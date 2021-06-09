package com.mptsix.todaydiary.view.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity() {
    private val binding: ActivityMapBinding by lazy {
        ActivityMapBinding.inflate(layoutInflater)
    }

    lateinit var googleMap: GoogleMap
    var loc = LatLng(37.5642135, 127.0016985)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initMap()
    }

    override fun onDestroy() {
        super.onDestroy()
        setResult(Activity.RESULT_CANCELED)
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // Create Marker Options
        val option: MarkerOptions = MarkerOptions().apply {
            position(loc)
            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        }

        // Init Map
        mapFragment.getMapAsync {
            googleMap = it.apply {
                animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f))
                setMinZoomPreference(6.0f)
                setMaxZoomPreference(18.0f)
                addMarker(option)?.showInfoWindow()
            }
            initMapListener()
        }
    }

    private fun initMapListener() {
        lateinit var location :LatLng
        googleMap.setOnMapClickListener {
            googleMap.clear()
            val option = MarkerOptions().apply {
                position(it)
                icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            }
            googleMap.addMarker(option)
            location = it
            val intent = Intent("${location.latitude},${location.longitude}")
            //Toast.makeText(this, location.toString(),Toast.LENGTH_SHORT).show() --> 여기까진 잘됨
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}