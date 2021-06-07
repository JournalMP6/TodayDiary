package com.mptsix.todaydiary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.data.response.UserFiltered
import com.mptsix.todaydiary.model.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class ProfileViewModel : ViewModel(){
    var sealedData : MutableLiveData<UserSealed> = MutableLiveData()
    var isPasswordChangeSucceed : MutableLiveData<Boolean> = MutableLiveData()
    var userRemoveSucceed : MutableLiveData<Boolean> = MutableLiveData()
    var followingUserList: MutableLiveData<List<UserFiltered>> = MutableLiveData()

    private fun<T> executeServerAndElse(serverCallCore: () -> T, onSuccess: suspend(successValue: T)->Unit, onFailure: suspend(failedThrowable: Throwable)->Unit) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    serverCallCore()
                }.onFailure {
                    Log.e(this::class.java.simpleName, "Error communicating with server")
                    Log.e(this::class.java.simpleName, it.stackTraceToString())
                    afterExecute(it, onFailure)
                }.onSuccess {
                    afterExecute(it, onSuccess)
                }
            }
        }
    }

    private suspend fun<T> afterExecute(successValue: T, afterServerReplyCallback: suspend(successValue: T)->Unit) {
        withContext(Dispatchers.Main) {
            afterServerReplyCallback(successValue)
        }
    }

    fun removeUser(){
        executeServerAndElse(
            serverCallCore = {ServerRepository.removeUser()},
            onSuccess = {userRemoveSucceed.value = true},
            onFailure = {userRemoveSucceed.value = false}
        )
    }

    fun getSealedData(){
        executeServerAndElse(
            serverCallCore = {ServerRepository.getSealedUser()},
            onSuccess = {sealedData.value = it},
            onFailure = {}
        )
    }

    fun changePassword(changePasswordRequest: PasswordChangeRequest){
        executeServerAndElse(
            serverCallCore = {ServerRepository.changePassword(changePasswordRequest)},
            onSuccess = {isPasswordChangeSucceed.value =true},
            onFailure = {isPasswordChangeSucceed.value =false}
        )
    }

    fun getFollowingUser() {
        executeServerAndElse(
            serverCallCore = {ServerRepository.getFollowingUser()},
            onSuccess = {followingUserList.value = it},
            onFailure = {}
        )
    }
}