package com.mptsix.todaydiary.viewmodel

import androidx.lifecycle.MutableLiveData

class LockViewModel: ViewModelHelper() {
    var isCheckSucceeds: MutableLiveData<Boolean> = MutableLiveData()

    fun registerAuxiliaryPassword(userPassword: String, _onFailure: (t: Throwable) -> Unit) {
        executeServerAndElse(
            serverCallCore = {},
            onSuccess = {},
            onFailure = {_onFailure(it)}
        )
    }

    fun checkAuxiliaryPassword(userPassword: String) {
        executeServerAndElse(
            serverCallCore = {},
            onSuccess = {isCheckSucceeds.value = true},
            onFailure = {isCheckSucceeds.value = false}
        )
    }
}