package com.mptsix.todaydiary.model

import com.mptsix.todaydiary.data.response.UserFiltered
import okhttp3.ResponseBody

interface ServerRepositoryInterface {
    fun findUserByUserName(userName: String): List<UserFiltered>
    fun followUser(userId: String)
    fun unfollowUser(userId: String)
    fun getFollowingUser(): List<UserFiltered>
}