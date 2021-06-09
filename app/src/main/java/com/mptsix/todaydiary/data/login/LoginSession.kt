package com.mptsix.todaydiary.data.login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_session")
class LoginSession(
    @PrimaryKey(autoGenerate = true) var id: Long?,
    @ColumnInfo(name = "userid") var userId: String,
    @ColumnInfo(name = "userPassword") var userPassword: String
)