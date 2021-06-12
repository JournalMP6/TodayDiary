package com.mptsix.todaydiary.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mptsix.todaydiary.data.login.LoginSessionRepository
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.model.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val serverRepository: ServerRepository
): ViewModel() {
    var isDirectedToLogin: MutableLiveData<Boolean> = MutableLiveData()
    private val loginSessionRepository: LoginSessionRepository by lazy {
        LoginSessionRepository.getRepository()
    }

    fun isLoginRedirectionNeeded(onCommunicationFailure: (t: Throwable) -> Unit) {
        val referenceStartTime: Long = System.currentTimeMillis()
        viewModelScope.launch {
            runCatching {
                loginSessionRepository.getUserSession()
            }.onFailure {
                // it does not have session yet.
                setLoginRedirection(true, referenceStartTime)
            }.onSuccess {
                withContext(Dispatchers.IO) {
                    runCatching {
                        serverRepository.loginRequest(
                            LoginRequest(
                                userId = it.userId,
                                userPassword = it.userPassword
                            )
                        )
                    }.onFailure {
                        Log.e(this::class.java.simpleName, "Failed to login: ${it.stackTraceToString()}")
                        withContext(Dispatchers.Main) {
                            onCommunicationFailure(it)
                        }
                        setLoginRedirection(true, referenceStartTime)
                    }.onSuccess {
                        setLoginRedirection(false, referenceStartTime)
                    }
                }
            }
        }
    }

    fun removeAllSessionData() {
        viewModelScope.launch {
            loginSessionRepository.removeAllEntries()
        }
    }

    private suspend fun setLoginRedirection(targetValue: Boolean, referenceStartTime: Long) {
        val endTime: Long = 3000 + referenceStartTime
        val toSleep: Long = System.currentTimeMillis()
        withContext(Dispatchers.IO) {
            if (toSleep <= endTime) {
                delay(endTime - toSleep)
            }
        }

        withContext(Dispatchers.Main) {
            isDirectedToLogin.value = targetValue
        }
    }
}