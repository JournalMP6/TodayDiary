package com.mptsix.todaydiary.viewmodel

import android.util.Log
import android.widget.TableRow
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mptsix.todaydiary.data.response.UserFiltered
import com.mptsix.todaydiary.model.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

// Follow - Unfollow, User ID, User Name
// IDEA: Find by

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val serverRepository: ServerRepository
): ViewModelHelper() {
    // User search result
    var userFilteredList: MutableLiveData<List<UserFiltered>> = MutableLiveData()
    // Whether following specific user is succed or not
    var isFollowSucceed: MutableLiveData<Boolean> = MutableLiveData()
    // Whether unfollowing specific user is succeed or not
    var isUnFollowSucceed: MutableLiveData<Boolean> = MutableLiveData()

    fun findUserByUserName(userName: String, _onFailure:(t:Throwable) -> Unit) {
        executeServerAndElse(
            serverCallCore = {serverRepository.findUserByUserName(userName)},
            onSuccess = {userFilteredList.value = it},
            onFailure = { _onFailure(it)}
        )
    }



    fun followUser(userId: String, _onFailure:(t:Throwable) -> Unit) {
        executeServerAndElse(
            serverCallCore = {serverRepository.followUser(userId)},
            onSuccess = {isFollowSucceed.value = true},
            onFailure = {
                isFollowSucceed.value = false
                _onFailure(it)
            }
        )
    }

    fun unfollowUser(userId: String, _onFailure:(t:Throwable) -> Unit) {
        executeServerAndElse(
            serverCallCore = {serverRepository.unfollowUser(userId)},
            onSuccess = {isUnFollowSucceed.value = true},
            onFailure = {
                isUnFollowSucceed.value = false
                _onFailure(it)
            }
        )
    }
}
