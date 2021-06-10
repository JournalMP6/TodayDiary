package com.mptsix.todaydiary.model

import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ServerAPI {
    @POST("/api/v1/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse> // login

    @POST("/api/v1/user")
    fun registerUser(@Body userRegisterRequest: UserRegisterRequest):Call<UserRegisterResponse> // sign up

    @POST("/api/v1/journal")
    fun registerJournal(@HeaderMap header:HashMap<String, Any?>, @Body journal: Journal): Call<JournalResponse> // regist journal

    @GET("/api/v1/journal/{time}")
    fun getJournal(@HeaderMap header: HashMap<String, Any?>, @Path("time") journalTime: Long): Call<Journal> // get journal

    @GET("/api/v1/user/sealed")
    fun getSealedUser(@HeaderMap header: HashMap<String, Any?>):Call<UserSealed>

    @PUT("/api/v1/user")
    fun changePassword(@HeaderMap header: HashMap<String, Any?>, @Body passwordChangeRequest: PasswordChangeRequest): Call<ResponseBody>

    @DELETE("/api/v1/user")
    fun removeUser(@HeaderMap header: HashMap<String, Any?>):Call<ResponseBody>

    @GET("/api/v1/user/{name}")
    fun findUserByName(@HeaderMap header: HashMap<String, Any?>, @Path("name") userName: String): Call<List<UserFiltered>>

    @POST("/api/v1/user/follow/{id}")
    fun followUser(@HeaderMap header: HashMap<String, Any?>, @Path("id") userId: String): Call<ResponseBody>

    @DELETE("/api/v1/user/follow/{id}")
    fun unfollowUser(@HeaderMap header: HashMap<String, Any?>, @Path("id") userId: String): Call<ResponseBody>

    @GET("/api/v1/user/follow")
    fun getFollowingUser(@HeaderMap header: HashMap<String, Any?>): Call<List<UserFiltered>>
    @GET("/api/v1/user/sealed/{userId}")
    fun getSealedUserByUserId(@HeaderMap header: HashMap<String, Any?>, @Path("userId") userId: String): Call<UserSealed>
}