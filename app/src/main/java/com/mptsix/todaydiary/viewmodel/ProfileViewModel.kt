package com.mptsix.todaydiary.viewmodel

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
            lateinit var userRemoveResponse : ResponseBody
            withContext(Dispatchers.IO){
                runCatching {
                    userRemoveResponse = ServerRepository.removeUser()
                }.onFailure {
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
            lateinit var sealData:UserSealed
            withContext(Dispatchers.IO){
                sealData = ServerRepository.getSealedUser()
            }
            withContext(Dispatchers.Main){
                sealedData.value = sealData
            }
        }
    }

    fun changePassword(changePasswordRequest: PasswordChangeRequest){
        viewModelScope.launch {
            lateinit var changePassword : ResponseBody
            withContext(Dispatchers.IO){
                runCatching {
                    changePassword = ServerRepository.changePassword(changePasswordRequest)
                }.onFailure {
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