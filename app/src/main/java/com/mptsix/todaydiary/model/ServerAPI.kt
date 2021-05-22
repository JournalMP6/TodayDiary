package com.mptsix.todaydiary.model

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ServerAPI {
    @FormUrlEncoded
    @POST("/api/v1/login")
    fun login(@Field("loginRequest") loginRequest: LoginRequest): Call<LoginResponse>

    @FormUrlEncoded
    @POST("/api/v1/user")
    fun registerUser(@Field("userRegisterRequest") userRegisterRequest: UserRegisterRequest):Call<UserRegisterResponse>
}