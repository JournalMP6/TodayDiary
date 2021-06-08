package com.mptsix.todaydiary.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.model.ServerRepository

class LogInViewModel: ViewModelHelper() {
    var loginSuccess = MutableLiveData<Boolean>()
    var registerSuccess = MutableLiveData<Boolean>()

    //login과 그 결과에 따른 LiveData에 정보 입력
    fun login(userLoginRequest: LoginRequest){
        executeServerAndElse(
            serverCallCore = {ServerRepository.loginRequest(userLoginRequest)},
            onSuccess = {loginSuccess.value = (it.userToken != "")},
            onFailure = {loginSuccess.value = false}
        )
    }

    //입력형식 정규식에 부합하는지 확인
    private fun inputValidation(strId:String, strPassword:String): Boolean {
        return (Patterns.EMAIL_ADDRESS.matcher(strId).matches() && strPassword.length>8)
    }

    fun register(userRegisterRequest: UserRegisterRequest) {
        if (!inputValidation(userRegisterRequest.userId, userRegisterRequest.userPassword)) {
            Log.e(this::class.java.simpleName, "Input validation did not succeed.")
            registerSuccess.value = false
            return
        }

        // Execute/Exchange with server.
        executeServerAndElse(
            serverCallCore = {ServerRepository.registerUser(userRegisterRequest)},
            onSuccess = {registerSuccess.value = (it.registeredId == userRegisterRequest.userId)},
            onFailure = {registerSuccess.value = false}
        )
    }
}
