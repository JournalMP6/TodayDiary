package com.mptsix.todaydiary.viewmodel

import androidx.lifecycle.MutableLiveData
import com.mptsix.todaydiary.model.ServerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LockViewModel @Inject constructor(
    private val serverRepository: ServerRepository
): ViewModelHelper() {
    var isCheckSucceeds: MutableLiveData<Boolean> = MutableLiveData()
    var auxPwRegisterSucceeds: MutableLiveData<Boolean> = MutableLiveData()

    fun registerAuxiliaryPassword(userPassword: String, _onFailure: (t: Throwable) -> Unit) {
        executeServerAndElse(
            serverCallCore = {serverRepository.registerAuxiliaryPassword(userPassword)},
            onSuccess = {auxPwRegisterSucceeds.value = true},
            onFailure = {
                _onFailure(it)
                auxPwRegisterSucceeds.value = false
            }
        )
    }

    fun checkAuxiliaryPassword(userPassword: String) {
        executeServerAndElse(
            serverCallCore = {serverRepository.checkAuxiliaryPassword(userPassword)},
            onSuccess = {isCheckSucceeds.value = true},
            onFailure = {isCheckSucceeds.value = false}
        )
    }
}