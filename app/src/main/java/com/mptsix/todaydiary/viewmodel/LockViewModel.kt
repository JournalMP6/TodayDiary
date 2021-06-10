package com.mptsix.todaydiary.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mptsix.todaydiary.model.ServerRepository

class LockViewModel: ViewModelHelper() {
    var isCheckSucceeds: MutableLiveData<Boolean> = MutableLiveData()
    var auxPwRegisterSucceeds: MutableLiveData<Boolean> = MutableLiveData()

    fun registerAuxiliaryPassword(userPassword: String, _onFailure: (t: Throwable) -> Unit) {
        executeServerAndElse(
            serverCallCore = {ServerRepository.registerAuxiliaryPassword(userPassword)},
            onSuccess = {auxPwRegisterSucceeds.value = true},
            onFailure = {
                _onFailure(it)
                auxPwRegisterSucceeds.value = false
            }
        )
    }

    fun checkAuxiliaryPassword(userPassword: String) {
        executeServerAndElse(
            serverCallCore = {ServerRepository.checkAuxiliaryPassword(userPassword)},
            onSuccess = {isCheckSucceeds.value = true},
            onFailure = {isCheckSucceeds.value = false}
        )
    }
}