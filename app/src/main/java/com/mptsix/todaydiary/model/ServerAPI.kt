package com.mptsix.todaydiary.model

import com.mptsix.todaydiary.data.JournalDto
import com.mptsix.todaydiary.data.JournalResponse
import com.mptsix.todaydiary.data.PictureResponse
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.LoginResponse
import com.mptsix.todaydiary.data.response.UserRegisterResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ServerAPI {
    @POST("/api/v1/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("/api/v1/user")
    fun registerUser(@Body userRegisterRequest: UserRegisterRequest):Call<UserRegisterResponse>

    @POST("/api/v1/journal")
    fun registerJournal(@HeaderMap header:HashMap<String, Any?>,
                        @Body journalDto: JournalDto
    ): Call<JournalResponse>

    @Multipart
    @POST("/api/v1/journal/picture")
    fun registerPicture(@HeaderMap header: HashMap<String, Any?>,
                        @Part files: MultipartBody.Part
    ):Call<PictureResponse>

    @GET("/api/v1/journal")
    fun getJournal(@HeaderMap header: HashMap<String, Any?>): Call<List<JournalDto>>
}