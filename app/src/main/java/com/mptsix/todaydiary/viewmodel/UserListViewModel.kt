package com.mptsix.todaydiary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mptsix.todaydiary.data.response.UserFiltered
import com.mptsix.todaydiary.model.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

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
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    ServerRepository.findUserByUserName(userName)
                }.onFailure {
                    withContext(Dispatchers.Main){
                        Log.e(this::class.java.simpleName, it.stackTrace.toString())
                    }
                }.onSuccess {
                    withContext(Dispatchers.Main){
                        userFilteredList.value = it
                    }
                }
            }
        }
    }

    fun followUser(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                runCatching {
                    ServerRepository.followUser(userId)
                }.onFailure {
                    withContext(Dispatchers.Main){
                        Log.e(this::class.java.simpleName,it.stackTrace.toString())
                        isFollowSucceed.value = false
                    }
                }.onSuccess {
                    withContext(Dispatchers.Main){
                        isFollowSucceed.value = true
                    }
                }
            }
        }
    }

    fun unfollowUser(userId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                runCatching {
                    ServerRepository.unfollowUser(userId)
                }.onFailure {
                    withContext(Dispatchers.Main){
                        Log.e(this::class.java.simpleName,it.stackTrace.toString())
                        isUnFollowSucceed.value = false
                    }
                }.onSuccess {
                    withContext(Dispatchers.Main){
                        isUnFollowSucceed.value = true
                    }
                }
            }
        }
    }
}
