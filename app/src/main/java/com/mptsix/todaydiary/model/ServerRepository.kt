package com.mptsix.todaydiary.model

import android.util.Log
import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerRepository: ServerRepositoryInterface {
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

    private fun getTokenHeader(): HashMap<String, Any?> = HashMap<String, Any?>().apply {
        put("X-AUTH-TOKEN", userToken)
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

    fun registerJournal(journal: Journal): JournalResponse {
        val registerJournalApi: Call<JournalResponse> = serverApi.registerJournal(getTokenHeader(), journal)
        return registerJournalApi.execute().body()!!
    }

    fun getJournal(journalDate: Long) : Journal{
        val requestJournalApi: Call<Journal> = serverApi.getJournal(getTokenHeader(), journalDate)

        return kotlin.runCatching {
            requestJournalApi.execute().body()!!
        }.getOrThrow()
    }

    fun getSealedUser():UserSealed{
        val getSealedUserApi:Call<UserSealed> = serverApi.getSealedUser(getTokenHeader())
        return kotlin.runCatching {
            getSealedUserApi.execute().body()!!
        }.getOrThrow()
    }

    fun changePassword(passwordChangeRequest: PasswordChangeRequest) {
        val changePasswordApi:Call<ResponseBody> = serverApi.changePassword(getTokenHeader(), passwordChangeRequest)
        kotlin.runCatching {
            changePasswordApi.execute()
        }.getOrThrow()
    }


    fun removeUser() {
        val removeUserApi: Call<ResponseBody> = serverApi.removeUser(getTokenHeader())

        kotlin.runCatching {
            removeUserApi.execute()
        }.getOrThrow()
    }

    override fun findUserByUserName(userName: String): List<UserFiltered> {
        val findUserApi:Call<List<UserFiltered>> = serverApi.findUserByName(getTokenHeader(), userName)

        return kotlin.runCatching {
            findUserApi.execute().body()!!
        }.getOrThrow()
    }

    override fun followUser(userId: String) {
        val followUserApi: Call<ResponseBody> = serverApi.followUser(getTokenHeader(), userId)

        kotlin.runCatching {
            followUserApi.execute()
        }
    }

    override fun unfollowUser(userId: String) {
        val unfollowApi : Call<ResponseBody> = serverApi.unfollowUser(getTokenHeader(), userId)

        kotlin.runCatching {
            unfollowApi.execute()
        }
    }

    override fun getFollowingUser(): List<UserFiltered> {
        val getFollowingApi : Call<List<UserFiltered>> = serverApi.getFollowingUser(getTokenHeader())

        return kotlin.runCatching {
            getFollowingApi.execute().body()!!
        }.getOrThrow()
    }
}