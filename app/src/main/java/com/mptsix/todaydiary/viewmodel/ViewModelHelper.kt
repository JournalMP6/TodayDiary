package com.mptsix.todaydiary.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class ViewModelHelper: ViewModel() {

    /**
     * executeServeAndElse - Exchange data with server and handle both onSuccess and onFailure situation.
     * Parameter:
     * serverCallCore: () -> T --> The Server Execution Code[ServerRepository.*]
     * onSuccess: suspend(successValue: T)->Unit --> When Server Execution is succeed[The result from serverCallCore() will be pushed to successValue]
     * onFailure: suspend(failedThrowable: Throwable)->Unit --> When server execution is failed. Throwable will be pushed to failedThrowable
     *
     * Note: This function handles both viewModelScope and its context. Function callee do not have to use withContext or *.launch
     */
    protected fun<T> executeServerAndElse(serverCallCore: suspend() -> T, onSuccess: suspend(successValue: T)->Unit, onFailure: suspend(failedThrowable: Throwable)->Unit) {
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