package com.mptsix.todaydiary.model.map

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MapRepository {
    private var instance: MapAPI? = null
    private val mapAPI: MapAPI get() = instance!!
    private const val URL = "https://maps.googleapis.com"
    private const val API_KEY = "" // TODO: Gradle-FY

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