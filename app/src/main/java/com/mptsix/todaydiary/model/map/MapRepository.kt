package com.mptsix.todaydiary.model.map

import com.mptsix.todaydiary.BuildConfig
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MapRepository @Inject constructor() {
    private var instance: MapAPI? = null
    private val mapAPI: MapAPI get() = instance!!
    private val URL = "https://maps.googleapis.com"
    private val API_KEY = BuildConfig.MAP_GEO_KEY

    init {
        getInstance()
    }

    private fun getInstance() {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MapAPI::class.java)
        }
    }

    fun getLocation(geoLocation: String): MapLocationResponse = MapRepositoryHelper.executeServer(
        apiFunction = mapAPI.getLocation(geoLocation, API_KEY)
    )
}