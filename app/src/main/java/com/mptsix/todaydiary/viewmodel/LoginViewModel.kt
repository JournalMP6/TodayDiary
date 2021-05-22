package com.mptsix.todaydiary.viewmodel

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.data.response.LoginResponse
import com.mptsix.todaydiary.data.response.UserRegisterResponse
import com.mptsix.todaydiary.model.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LogInViewModel: ViewModel() {
    var loginSuccess = MutableLiveData<Boolean>()
    var registerSuccess = MutableLiveData<Boolean>()

    //login과 그 결과에 따른 LiveData에 정보 입력
    fun login(userLoginRequest: LoginRequest){
        viewModelScope.launch {
            lateinit var userLoginResponse: LoginResponse
            withContext(Dispatchers.IO) {
                userLoginResponse = ServerRepository.loginRequest(userLoginRequest)
            }

            withContext(Dispatchers.Main) {
                loginSuccess.value = (userLoginResponse.userToken != "")
            }
        }
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

        viewModelScope.launch {
            lateinit var userRegisterResponse: UserRegisterResponse
            withContext(Dispatchers.IO) {
                userRegisterResponse = ServerRepository.registerUser(userRegisterRequest)
            }

            withContext(Dispatchers.Main) {
                registerSuccess.value = (userRegisterResponse.registeredId == userRegisterRequest.userId)
            }
        }

    }
}
