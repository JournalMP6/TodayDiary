package com.mptsix.todaydiary.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.data.response.UserFiltered
import com.mptsix.todaydiary.model.ServerRepository

class ProfileViewModel : ViewModelHelper() {
    var sealedData : MutableLiveData<UserSealed> = MutableLiveData()
    var isPasswordChangeSucceed : MutableLiveData<Boolean> = MutableLiveData()
    var userRemoveSucceed : MutableLiveData<Boolean> = MutableLiveData()
    var followingUserList: MutableLiveData<List<UserFiltered>> = MutableLiveData()

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