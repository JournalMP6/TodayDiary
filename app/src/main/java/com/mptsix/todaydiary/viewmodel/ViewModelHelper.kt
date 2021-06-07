package com.mptsix.todaydiary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class ViewModelHelper: ViewModel() {
    protected fun<T> executeServerAndElse(serverCallCore: () -> T, onSuccess: suspend(successValue: T)->Unit, onFailure: suspend(failedThrowable: Throwable)->Unit) {
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
}