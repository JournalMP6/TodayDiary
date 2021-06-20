package com.mptsix.todaydiary.data.internal

data class PasswordChangeRequest(
    var previousPassword: String,
    var userPassword: String,
)