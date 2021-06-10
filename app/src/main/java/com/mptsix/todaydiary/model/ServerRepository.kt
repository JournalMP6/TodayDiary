package com.mptsix.todaydiary.model

import android.util.Log
import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.*
import com.mptsix.todaydiary.model.ServerRepositoryHelper.executeServer
import com.mptsix.todaydiary.model.ServerRepositoryHelper.handle204
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServerRepository: ServerRepositoryInterface {
    private var instance: ServerAPI? = null
    private val serverApi: ServerAPI get() = instance!!
    private var userToken: String? = null
    private const val URL = "http://192.168.0.46:8080"

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

    fun loginRequest(loginRequest: LoginRequest): LoginResponse = executeServer(
        apiFunction = serverApi.login(loginRequest)
    ).also {
        userToken = it.userToken
    }

    fun registerUser(userRegisterRequest: UserRegisterRequest): UserRegisterResponse = executeServer(
        apiFunction = serverApi.registerUser(userRegisterRequest)
    )

    fun registerJournal(journal: Journal): JournalResponse = executeServer(
        apiFunction = serverApi.registerJournal(getTokenHeader(), journal)
    )

    fun getJournal(journalDate: Long) : Journal = executeServer(
        apiFunction = serverApi.getJournal(getTokenHeader(), journalDate)
    )

    fun getSealedUser():UserSealed = executeServer(
        apiFunction = serverApi.getSealedUser(getTokenHeader())
    )

    fun changePassword(passwordChangeRequest: PasswordChangeRequest) = handle204 {
        executeServer(
            apiFunction = serverApi.changePassword(getTokenHeader(), passwordChangeRequest)
        )
    }.apply {
        userToken = null
    }

    fun removeUser() = handle204 {
        executeServer(
            apiFunction = serverApi.removeUser(getTokenHeader())
        )
    }.also {
        userToken = null
    }

    override fun findUserByUserName(userName: String): List<UserFiltered> = executeServer(
        apiFunction = serverApi.findUserByName(getTokenHeader(), userName)
    )

    override fun followUser(userId: String) = handle204 {
        executeServer(
            apiFunction = serverApi.followUser(getTokenHeader(), userId)
        )
    }

    override fun unfollowUser(userId: String) = handle204 {
        executeServer(
            apiFunction = serverApi.unfollowUser(getTokenHeader(), userId)
        )
    }

    override fun getFollowingUser(): List<UserFiltered> = executeServer(
        apiFunction = serverApi.getFollowingUser(getTokenHeader())
    )
    fun getSealedUserByUserId(userId: String): UserSealed = executeServer(
        apiFunction = serverApi.getSealedUserByUserId(getTokenHeader(), userId)
    )
}