package com.mptsix.todaydiary.data.response

// 유저 검색용!!
data class UserFiltered(
    var userName: String,
    var userId: String,
    var isUserFollowedTargetUser: Boolean // true 이면 UserSealed 유저와 현재 사용중인 유저가 서로 Follow 관계.
)