package com.mptsix.todaydiary.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mptsix.todaydiary.data.internal.DiaryWriteMode
import com.mptsix.todaydiary.data.response.Journal
import com.mptsix.todaydiary.data.response.JournalResponse
import com.mptsix.todaydiary.model.ServerRepository
import com.mptsix.todaydiary.model.map.MapLocationResponse
import com.mptsix.todaydiary.model.map.MapRepository
import com.mptsix.todaydiary.transition.DisplayTransition
import com.mptsix.todaydiary.transition.Transition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val serverRepository: ServerRepository
): ViewModelHelper() {
    var isJournalSubmit : MutableLiveData<Boolean> = MutableLiveData()
    var isJournalEdited : MutableLiveData<Boolean> = MutableLiveData()
    var displayTransition: MutableLiveData<Transition> = MutableLiveData()
    var journalLocation: MutableLiveData<MapLocationResponse> = MutableLiveData()

    // Journal Cache
    var journalCache: Journal? = null

    // Used in DiaryFragment
    var isJournalExistsByTimeStamp: MutableLiveData<Journal> = MutableLiveData()

    fun getByteArray(uri: Uri, context: Context): ByteArray {
        val fileInputStream: InputStream = context.contentResolver.openInputStream(uri) ?: run {
            Log.e(this::class.java.simpleName, "Cannot open input stream. Input Stream returned NULL!")
            throw IllegalStateException("Cannot open input stream. Input Stream returned NULL!")
        }

        return fileInputStream.readBytes()
    }

    fun registerJournal(journal: Journal, _onFailure:(t:Throwable)->Unit) {
        executeServerAndElse(
            serverCallCore = {serverRepository.registerJournal(journal)},
            onSuccess = {isJournalSubmit.value = true},
            onFailure = {
                _onFailure(it)
                isJournalSubmit.value = false
            }
        )
    }

    fun isJournalExists(timeStamp: Long, _onFailure:(t:Throwable)->Unit) {
        executeServerAndElse(
            serverCallCore = {serverRepository.getJournal(timeStamp)},
            onSuccess = {isJournalExistsByTimeStamp.value = it},
            onFailure = {
                isJournalExistsByTimeStamp.value = null
                _onFailure(it)
            }
        )
    }

    fun getLocationFromGeo(geoLocation: String, _onFailure:(t:Throwable)->Unit) {
        executeServerAndElse(
            serverCallCore = {MapRepository.getLocation(geoLocation)},
            onSuccess = {journalLocation.value = it},
            onFailure = {_onFailure(it)}
        )
    }

    fun requestDiaryPage(timeStamp: Long) {
        displayTransition.value = Transition(
            DisplayTransition.REQUEST_DIARY,
            timeStamp
        )
    }

    fun requestEditPage(diaryWriteMode: DiaryWriteMode){
        displayTransition.value = Transition(
            DisplayTransition.REQUEST_EDIT,
            diaryWriteMode
        )
    }

    fun requestUserInfoPage() {
        displayTransition.value = Transition(
            DisplayTransition.REQUEST_USERINFO
        )
    }

    fun requestUserSearchPage(){
        displayTransition.value = Transition(
            DisplayTransition.REQUEST_SEARCH
        )
    }
}