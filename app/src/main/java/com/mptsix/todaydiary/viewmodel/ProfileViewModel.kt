package com.mptsix.todaydiary.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.data.login.LoginSessionRepository
import com.mptsix.todaydiary.data.response.UserFiltered
import com.mptsix.todaydiary.model.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val serverRepository: ServerRepository,
    private val loginSessionRepository: LoginSessionRepository
) : ViewModelHelper() {
    var sealedData : MutableLiveData<UserSealed> = MutableLiveData()
    var isPasswordChangeSucceed : MutableLiveData<Boolean> = MutableLiveData()
    var userRemoveSucceed : MutableLiveData<Boolean> = MutableLiveData()
    var followingUserList: MutableLiveData<List<UserFiltered>> = MutableLiveData()
    var sealedData2 : MutableLiveData<UserSealed> = MutableLiveData()

    fun removeUser(_onFailure:(t:Throwable)->Unit){
        executeServerAndElse(
            serverCallCore = {serverRepository.removeUser()},
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
            serverCallCore = {serverRepository.getSealedUser()},
            onSuccess = {sealedData.value = it},
            onFailure = {
                _onFailure(it)
            }
        )
    }

    fun getSealedUserByUserId(userId:String, _onFailure:(t:Throwable)->Unit) {
        executeServerAndElse(
            serverCallCore = {serverRepository.getSealedUserByUserId(userId)},
            onSuccess = {sealedData2.value=it},
            onFailure = {_onFailure(it)}
        )
    }

    fun changePassword(changePasswordRequest: PasswordChangeRequest, _onFailure:(t:Throwable)->Unit){
        executeServerAndElse(
            serverCallCore = {serverRepository.changePassword(changePasswordRequest)},
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
            serverCallCore = {serverRepository.getFollowingUser()},
            onSuccess = {followingUserList.value = it},
            onFailure = { _onFailure(it)}
        )
    }
}