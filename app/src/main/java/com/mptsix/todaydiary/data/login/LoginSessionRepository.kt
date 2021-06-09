package com.mptsix.todaydiary.data.login

import android.content.Context
import android.util.Log
import androidx.room.Room

class LoginSessionRepository(
    private val loginSessionDao: LoginSessionDao
) {
    // For Creating Singleton Pattern
    companion object {
        private var loginSessionRepository: LoginSessionRepository? = null
        private var loginSessionDatabase: LoginSessionDatabase? = null

        fun initiateRepository(context: Context) {
            if (loginSessionDatabase == null) {
                Log.d(this::class.java.simpleName, "Creating ROOM Repository")
                loginSessionDatabase = Room.databaseBuilder(
                    context,
                    LoginSessionDatabase::class.java,
                    "login_session.db"
                ).build()
            } else {
                Log.d(this::class.java.simpleName, "Login Session Database instance is already exists.. skipping creating it.")
            }

            if (loginSessionRepository == null) {
                Log.d(this::class.java.simpleName, "Creating Session Repository")
                loginSessionRepository = LoginSessionRepository(loginSessionDatabase!!.getLoginSessionDao())
            } else {
                Log.d(this::class.java.simpleName, "Login Session Repository instance is already exists.. skipping creating it.")
            }
        }

        fun getRepository(): LoginSessionRepository {
            if (loginSessionDatabase == null || loginSessionRepository == null) {
                throw IllegalStateException("Either Login Session db or Login Session repository is null! Perhaps you did not called initiateRepository?")
            }

            return loginSessionRepository!!
        }
    }

    suspend fun addLoginSession(userId: String, userPassword: String) {
        loginSessionDao.addLoginSession(
            LoginSession(
                userId = userId,
                userPassword = userPassword,
                id = null
            )
        )
    }

    suspend fun removeAllEntries() {
        loginSessionDao.deleteAllEntries()
    }

    suspend fun getUserSession(): LoginSession {
        val loginSessionList: List<LoginSession> = loginSessionDao.getAllSession()
        if (loginSessionList.size != 1) {
            Log.e(this::class.java.simpleName, "Login session should be exactly one.")
            throw IllegalStateException("Login session should be exactly 1, but we found ${loginSessionList.size}")
        }

        return loginSessionList[0]
    }
}