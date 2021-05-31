package com.mptsix.todaydiary.model

import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.Journal
import com.mptsix.todaydiary.data.response.JournalResponse
import com.mptsix.todaydiary.data.response.LoginResponse
import com.mptsix.todaydiary.data.response.UserRegisterResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServerAPI {
    @POST("/api/v1/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/api/v1/user")
    fun registerUser(@Body userRegisterRequest: UserRegisterRequest):Call<UserRegisterResponse>

    @POST("/api/v1/journal")
    fun registerJournal(@HeaderMap header:HashMap<String, Any?>, @Body journal: Journal): Call<JournalResponse>

    @GET("/api/v1/journal/{time}")
    fun getJournal(@HeaderMap header: HashMap<String, Any?>, @Path("time") journalTime: Long): Call<Journal>

    @PUT("/api/v1/journal")
    fun editJournal(@HeaderMap header: HashMap<String, Any?>, @Body journal: Journal): Call<ResponseBody>
}