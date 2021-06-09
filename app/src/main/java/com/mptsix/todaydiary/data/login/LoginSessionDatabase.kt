package com.mptsix.todaydiary.data.login

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [LoginSession::class],
    version = 1
)
abstract class LoginSessionDatabase: RoomDatabase() {
    abstract fun getLoginSessionDao(): LoginSessionDao
}