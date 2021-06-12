package com.mptsix.todaydiary.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mptsix.todaydiary.data.login.LoginSessionRepository
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.model.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.RuntimeException
import java.net.ConnectException
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val serverRepository: ServerRepository
): ViewModelHelper() {
    var loginSuccess = MutableLiveData<Boolean>()
    var registerSuccess = MutableLiveData<Boolean>()

    private var loginSession: LoginSessionRepository = LoginSessionRepository.getRepository()

    //login과 그 결과에 따른 LiveData에 정보 입력
    fun login(userLoginRequest: LoginRequest, _onFailure:(t:Throwable)->Unit){
        executeServerAndElse(
            serverCallCore = {serverRepository.loginRequest(userLoginRequest)},
            onSuccess = {loginSuccess.value = (it.userToken != "")},
            onFailure = {
                _onFailure(it)
                loginSuccess.value = false
            }
        )
    }

    //입력형식 정규식에 부합하는지 확인
    private fun inputValidation(strId:String, strPassword:String): Boolean {
        return (Patterns.EMAIL_ADDRESS.matcher(strId).matches() && strPassword.length>8)
    }

    fun register(
        userRegisterRequest: UserRegisterRequest,
        _invalidFailure: () -> Unit,
        _onFailure: (t:Throwable) -> Unit
    ) {
        if (!inputValidation(userRegisterRequest.userId, userRegisterRequest.userPassword)) {
            Log.e(this::class.java.simpleName, "Input validation did not succeed.")
            _invalidFailure()
            registerSuccess.value = false
            return
        }

        // Execute/Exchange with server.
        executeServerAndElse(
            serverCallCore = {serverRepository.registerUser(userRegisterRequest)},
            onSuccess = {registerSuccess.value = (it.registeredId == userRegisterRequest.userId)},
            onFailure = {
                _onFailure(it)
                registerSuccess.value = false
            }
        )
    }

    fun registerIdToDb(userID: String, userPassword: String) {
        viewModelScope.launch {
            runCatching {
                loginSession.findLoginSessionByUserId(userID)
            }.onSuccess {
                // Already exists, do not do anything
            }.onFailure {
                loginSession.addLoginSession(userID, userPassword)
            }
        }
    }

    fun removeAllSession() {
        viewModelScope.launch {
            loginSession.removeAllEntries()
        }
    }
}
