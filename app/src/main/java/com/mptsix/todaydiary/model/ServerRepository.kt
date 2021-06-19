package com.mptsix.todaydiary.model

import android.util.Log
import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.data.request.AuxiliaryPasswordRequest
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.*
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ServerRepository(
    private val serverRepositoryHelper: ServerRepositoryHelper
): ServerRepositoryInterface {
    private var instance: ServerAPI? = null
    private val serverApi: ServerAPI get() = instance!!
    private var userToken: String? = null
    var userSealed: UserSealed? = null
    private val URL = "http://kuspace.mynetgear.com:567"

    init {
        getInstance()
    }

    private fun getInstance() {
        if (instance == null) {
            val okHttpClient: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build()
            instance = Retrofit.Builder()
                .baseUrl(URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServerAPI::class.java)
        }
    }

    private fun getTokenHeader(): HashMap<String, Any?> = HashMap<String, Any?>().apply {
        put("X-AUTH-TOKEN", userToken)
    }

    fun loginRequest(loginRequest: LoginRequest): LoginResponse = serverRepositoryHelper.executeServer(
        apiFunction = serverApi.login(loginRequest)
    ).also {
        userToken = it.userToken
        userSealed = getSealedUser()
    }

    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse = serverRepositoryHelper.executeServer(
        apiFunction = serverApi.registerUser(userRegisterRequest)
    )

    fun registerJournal(journal: Journal): JournalResponse = serverRepositoryHelper.executeServer(
        apiFunction = serverApi.registerJournal(getTokenHeader(), journal)
    )

    fun getJournal(journalDate: Long) : Journal = serverRepositoryHelper.executeServer(
        apiFunction = serverApi.getJournal(getTokenHeader(), journalDate)
    )

    fun getSealedUser():UserSealed = serverRepositoryHelper.executeServer(
        apiFunction = serverApi.getSealedUser(getTokenHeader())
    )

    fun changePassword(passwordChangeRequest: PasswordChangeRequest) = serverRepositoryHelper.handle204 {
        serverRepositoryHelper.executeServer(
            apiFunction = serverApi.changePassword(getTokenHeader(), passwordChangeRequest)
        )
    }.apply {
        userToken = null
        userSealed = null
    }

    fun removeUser() = serverRepositoryHelper.handle204 {
        serverRepositoryHelper.executeServer(
            apiFunction = serverApi.removeUser(getTokenHeader())
        )
    }.also {
        userToken = null
        userSealed = null
    }

    override fun findUserByUserName(userName: String): List<UserFiltered> = serverRepositoryHelper.executeServer(
        apiFunction = serverApi.findUserByName(getTokenHeader(), userName)
    )

    override fun followUser(userId: String) = serverRepositoryHelper.handle204 {
        serverRepositoryHelper.executeServer(
            apiFunction = serverApi.followUser(getTokenHeader(), userId)
        )
    }

    override fun unfollowUser(userId: String) = serverRepositoryHelper.handle204 {
        serverRepositoryHelper.executeServer(
            apiFunction = serverApi.unfollowUser(getTokenHeader(), userId)
        )
    }

    override fun getFollowingUser(): List<UserFiltered> = serverRepositoryHelper.executeServer(
        apiFunction = serverApi.getFollowingUser(getTokenHeader())
    )

    override fun registerAuxiliaryPassword(userPassword: String) = serverRepositoryHelper.handle204 {
        serverRepositoryHelper.executeServer(
            apiFunction = serverApi.registerAuxiliaryPassword(getTokenHeader(), AuxiliaryPasswordRequest(userPassword))
        )
    }

    override fun checkAuxiliaryPassword(userPassword: String) {
        val checkAuxiliaryPasswordApi = serverApi.checkAuxiliaryPassword(getTokenHeader(), AuxiliaryPasswordRequest(userPassword))
        val response:Response<ResponseBody> = kotlin.runCatching {
            checkAuxiliaryPasswordApi.execute()
        }.getOrElse {
            Log.e("TEST", "Error when getting root token from server.")
            Log.e("TEST", it.stackTraceToString())
            throw it
        }
        if(!response.isSuccessful){
            throw Exception("Password does not match")
        }
    }

    fun getSealedUserByUserId(userId: String): UserSealed = serverRepositoryHelper.executeServer(
        apiFunction = serverApi.getSealedUserByUserId(getTokenHeader(), userId)
    )

    fun logOut() {
        userToken = null
        userSealed = null
    }
}