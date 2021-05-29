package com.mptsix.todaydiary.model

import android.content.Context
import android.net.Uri
import com.mptsix.todaydiary.data.request.JournalDto
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.JournalResponse
import com.mptsix.todaydiary.data.response.LoginResponse
import com.mptsix.todaydiary.data.response.UserRegisterResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.InputStream
import java.nio.file.Files

object ServerRepository {
    private var instance: ServerAPI? = null
    private val serverApi: ServerAPI get() = instance!!
    private var userToken: String? = null
    private const val URL = "http://192.168.25.40:8080"

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

    fun registerJournal(journalDto: JournalDto) : JournalResponse {
        val registerJournalApi:Call<JournalResponse> = serverApi.registerJournal(getTokenHeader(), journalDto)

        return kotlin.runCatching {
            registerJournalApi.execute().body()!!
        }.getOrThrow()
    }

    fun registerPicture(body: MultipartBody.Part, journalDto: JournalDto) {
        val registerPictureApi:Call<ResponseBody> = serverApi.registerPicture(
            getTokenHeader().apply {
                put("JOURNAL-DATE", journalDto.journalDate)
            },
            body
        )

        // Register to Server
        registerPictureApi.execute().body()!!
    }

    fun getJournal() :List<JournalDto>{
        val requestJournalApi: Call<List<JournalDto>> = serverApi.getJournal(getTokenHeader())

        return kotlin.runCatching {
            requestJournalApi.execute().body()!!
        }.getOrThrow()
    }

    fun editJournal(journalDto: JournalDto) {
        val editJournalApi: Call<ResponseBody> =
            serverApi.editJournal(getTokenHeader(), journalDto)

        editJournalApi.execute()
    }

    private fun getTokenHeader(): HashMap<String, Any?> = HashMap<String, Any?>().apply {
        put("X-AUTH-TOKEN", userToken)
    }
}