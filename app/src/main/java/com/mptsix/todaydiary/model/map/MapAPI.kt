package com.mptsix.todaydiary.model.map

import retrofit2.Call
import retrofit2.http.*

data class InnerMapLocation(
    var formatted_address: String
)

data class MapLocationResponse(
    var results: List<InnerMapLocation>
)

interface MapAPI {
    @POST("/maps/api/geocode/json")
    fun getLocation(@Query("latlng") locationInfo: String, @Query("key") apikey: String) : Call<MapLocationResponse>
}