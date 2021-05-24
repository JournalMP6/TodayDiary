package com.mptsix.todaydiary.model

import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.LoginResponse
import com.mptsix.todaydiary.data.response.UserRegisterResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerRepository {
    private var instance: ServerAPI? = null
    private val serverApi: ServerAPI get() = instance!!
    private var userToken: String? = null
    private const val URL = "http://192.168.0.46:8080"

    init {
        getInstance()
    }

    private fun getInstance() {
        if (instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServerAPI::class.java)
        }
    }

    fun loginRequest(loginRequest: LoginRequest): LoginResponse {
        val callObject: Call<LoginResponse> = serverApi.login(loginRequest)

        return runCatching {
            callObject.execute().body()!!.also {
                userToken = it.userToken
            }
        }.getOrThrow()
    }

    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse {
        val registerApi: Call<UserRegisterResponse> = serverApi.registerUser(userRegisterRequest)

        return runCatching {
            registerApi.execute().body()!!
        }.getOrThrow()
    }
}