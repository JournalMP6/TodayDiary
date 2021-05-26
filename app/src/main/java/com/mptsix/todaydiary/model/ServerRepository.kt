package com.mptsix.todaydiary.model

import android.net.Uri
import android.util.Log
import com.mptsix.todaydiary.data.JournalDto
import com.mptsix.todaydiary.data.JournalResponse
import com.mptsix.todaydiary.data.PictureResponse
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.LoginResponse
import com.mptsix.todaydiary.data.response.UserRegisterResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.InputStream

object ServerRepository {
    private var instance: ServerAPI? = null
    private val serverApi: ServerAPI get() = instance!!
    private var userToken: String? = null
    private const val URL = "http://192.168.35.165:8080"

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

    fun registerJournal(journalDto: JournalDto) : JournalResponse{
        val header = HashMap<String, Any?>()
        header.put("X-AUTH-TOKEN", userToken)

        val registerJournalApi:Call<JournalResponse> = serverApi.registerJournal(header, journalDto)

        return kotlin.runCatching {
            registerJournalApi.execute().body()!!
        }.getOrThrow()
    }

    fun registerPicture(imgUri :Uri, journalDto: JournalDto) : PictureResponse{
        val header = HashMap<String, Any?>()
        header.put("X-AUTH-TOKEN", userToken)
        header.put("JOURNAL_DATE", journalDto.journalDate)

        val file = File(imgUri.path)

        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        val registerPictureApi:Call<PictureResponse> = serverApi.registerPicture(header, body)

        return kotlin.runCatching {
            registerPictureApi.execute().body()!!
        }.getOrThrow()



    }

    fun getJournal() :List<JournalDto>{
        val header = HashMap<String, Any?>()
        header.put("X-AUTH-TOKEN", userToken)

        val requestJournalApi: Call<List<JournalDto>> = serverApi.getJournal(header)

        return kotlin.runCatching {
            requestJournalApi.execute().body()!!
        }.getOrThrow()
    }
}