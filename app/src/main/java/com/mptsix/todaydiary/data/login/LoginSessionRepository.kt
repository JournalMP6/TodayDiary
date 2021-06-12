package com.mptsix.todaydiary.data.login

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginSessionRepository @Inject constructor(
    private val loginSessionDao: LoginSessionDao
) {

    suspend fun addLoginSession(userId: String, userPassword: String) {
        loginSessionDao.addLoginSession(
            LoginSession(
                userId = userId,
                userPassword = userPassword,
                id = null
            )
        )
    }

    suspend fun findLoginSessionByUserId(userId: String): LoginSession {
        val loginSessionList: List<LoginSession> = loginSessionDao.findLoginSessionByUserId(userId)

        // If nothing exists
        if (loginSessionList.isEmpty()) {
            throw NullPointerException("No User list are found!")
        } else if (loginSessionList.size != 1) {
            throw IllegalStateException("User list should be exactly one, but we found ${loginSessionList.size}")
        }

        return loginSessionList[0]
    }

    suspend fun removeAllEntries() {
        loginSessionDao.deleteAllEntries()
    }

    suspend fun getUserSession(): LoginSession {
        val loginSessionList: List<LoginSession> = loginSessionDao.getAllSession()
        if (loginSessionList.size != 1) {
            Log.e(this::class.java.simpleName, "Login session should be exactly one, we found ${loginSessionList.size}")
            throw IllegalStateException("Login session should be exactly 1, but we found ${loginSessionList.size}")
        }

        return loginSessionList[0]
    }
}