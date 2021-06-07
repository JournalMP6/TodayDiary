package com.mptsix.todaydiary.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mptsix.todaydiary.data.response.UserFiltered
import com.mptsix.todaydiary.model.ServerRepository

// Follow - Unfollow, User ID, User Name
// IDEA: Find by
class UserListViewModel: ViewModel() {
    // User search result
    var userFilteredList: MutableLiveData<List<UserFiltered>> = MutableLiveData()

    // Whether following specific user is succed or not
    var isFollowSucceed: MutableLiveData<Boolean> = MutableLiveData()

    // Whether unfollowing specific user is succeed or not
    var isUnFollowSucceed: MutableLiveData<Boolean> = MutableLiveData()

    fun findUserByUserName(userName: String) {
        ServerRepository.findUserByUserName(userName)
    }

    fun followUser(userId: String) {
        ServerRepository.followUser(userId)
    }

    fun unfollowUser(userId: String) {
        ServerRepository.unfollowUser(userId)
    }
}