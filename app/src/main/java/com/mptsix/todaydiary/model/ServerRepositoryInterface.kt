package com.mptsix.todaydiary.model

import com.mptsix.todaydiary.data.response.UserFiltered

interface ServerRepositoryInterface {
    fun findUserByUserName(userName: String): List<UserFiltered>
    fun followUser(userId: String)
    fun unfollowUser(userId: String)
    fun getFollowingUser(): List<UserFiltered>
}