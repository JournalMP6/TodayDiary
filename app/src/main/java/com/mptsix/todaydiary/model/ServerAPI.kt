package com.mptsix.todaydiary.model

import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.LoginResponse
import com.mptsix.todaydiary.data.response.UserRegisterResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerAPI {
    @POST("/api/v1/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/api/v1/user")
    fun registerUser(@Body userRegisterRequest: UserRegisterRequest):Call<UserRegisterResponse>
}