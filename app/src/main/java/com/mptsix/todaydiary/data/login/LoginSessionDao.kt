package com.mptsix.todaydiary.data.login

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoginSessionDao {
    @Query("SELECT * FROM login_session")
    suspend fun getAllSession(): List<LoginSession>

    @Query("SELECT * FROM login_session WHERE userid=:userIdInput")
    suspend fun findLoginSessionByUserId(userIdInput: String): List<LoginSession>

    @Insert
    suspend fun addLoginSession(session: LoginSession)

    @Query("DELETE FROM login_session")
    suspend fun deleteAllEntries()
}