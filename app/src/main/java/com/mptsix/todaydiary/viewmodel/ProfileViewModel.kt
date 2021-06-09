package com.mptsix.todaydiary.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.data.login.LoginSessionRepository
import com.mptsix.todaydiary.data.response.UserFiltered
import com.mptsix.todaydiary.model.ServerRepository

class ProfileViewModel : ViewModelHelper() {
    var sealedData : MutableLiveData<UserSealed> = MutableLiveData()
    var isPasswordChangeSucceed : MutableLiveData<Boolean> = MutableLiveData()
    var userRemoveSucceed : MutableLiveData<Boolean> = MutableLiveData()
    var followingUserList: MutableLiveData<List<UserFiltered>> = MutableLiveData()

    private val loginSessionRepository: LoginSessionRepository = LoginSessionRepository.getRepository()

    fun removeUser(_onFailure:(t:Throwable)->Unit){
        executeServerAndElse(
            serverCallCore = {ServerRepository.removeUser()},
            onSuccess = {
                // Clear out all session data first
                loginSessionRepository.removeAllEntries()
                userRemoveSucceed.value = true
            },
            onFailure = {
                userRemoveSucceed.value = false
                _onFailure(it)
            }
        )
    }

    fun getSealedData(_onFailure:(t:Throwable)->Unit){
        executeServerAndElse(
            serverCallCore = {ServerRepository.getSealedUser()},
            onSuccess = {sealedData.value = it},
            onFailure = {
                _onFailure(it)
            }
        )
    }

    fun changePassword(changePasswordRequest: PasswordChangeRequest, _onFailure:(t:Throwable)->Unit){
        executeServerAndElse(
            serverCallCore = {ServerRepository.changePassword(changePasswordRequest)},
            onSuccess = {
                // Clear out all session data first
                loginSessionRepository.removeAllEntries()
                isPasswordChangeSucceed.value =true
            },
            onFailure = {
                isPasswordChangeSucceed.value =false
                _onFailure(it)
                }
        )
    }

    fun getFollowingUser(_onFailure:(t:Throwable)->Unit) {
        executeServerAndElse(
            serverCallCore = {ServerRepository.getFollowingUser()},
            onSuccess = {followingUserList.value = it},
            onFailure = { _onFailure(it)}
        )
    }
}