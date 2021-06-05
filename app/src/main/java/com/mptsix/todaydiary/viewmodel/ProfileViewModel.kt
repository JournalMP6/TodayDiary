package com.mptsix.todaydiary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.model.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class ProfileViewModel : ViewModel(){
    var sealedData : MutableLiveData<UserSealed> = MutableLiveData()
    var isPasswordChangeSucceed : MutableLiveData<Boolean> = MutableLiveData()
    var userRemoveSucceed : MutableLiveData<Boolean> = MutableLiveData()

    fun removeUser(){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                runCatching {
                    ServerRepository.removeUser()
                }.onFailure {
                    Log.e(this::class.java.simpleName, it.stackTraceToString())
                    withContext(Dispatchers.Main){
                        userRemoveSucceed.value = false
                    }
                }.onSuccess {
                    withContext(Dispatchers.Main){
                        userRemoveSucceed.value = true
                    }
                }
            }
        }
    }

    fun getSealedData(){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    ServerRepository.getSealedUser()
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        sealedData.value = it
                    }
                }.onFailure {
                    Log.e(this::class.java.simpleName, it.stackTraceToString())
                }
            }
        }
    }

    fun changePassword(changePasswordRequest: PasswordChangeRequest){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                runCatching {
                    ServerRepository.changePassword(changePasswordRequest)
                }.onFailure {
                    Log.e(this::class.java.simpleName, it.stackTraceToString())
                    withContext(Dispatchers.Main){
                        isPasswordChangeSucceed.value =false
                    }
                }.onSuccess {
                    withContext(Dispatchers.Main){
                        isPasswordChangeSucceed.value =true
                    }
                }
            }
        }
    }
}