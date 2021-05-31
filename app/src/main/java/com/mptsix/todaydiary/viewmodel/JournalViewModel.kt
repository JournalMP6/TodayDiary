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
import com.mptsix.todaydiary.transition.DisplayTransition
import com.mptsix.todaydiary.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class JournalViewModel: ViewModel(){
    var isJournalSubmit : MutableLiveData<Boolean> = MutableLiveData()
    var isJournalEdited : MutableLiveData<Boolean> = MutableLiveData()
    var displayTransition: MutableLiveData<Transition> = MutableLiveData()

    // Journal Cache
    var journalCache: Journal? = null

    // Used in DiaryFragment
    var isJournalExistsByTimeStamp: MutableLiveData<Journal> = MutableLiveData()

    fun getByteArray(uri: Uri, context: Context): ByteArray {
        val file: File = File.createTempFile("SOME_RANDOM_IMAGE",null,context.cacheDir).apply {
            deleteOnExit()
        }
        val fileInputStream: InputStream = context.contentResolver.openInputStream(uri)!!

        FileOutputStream(file).use { outputStream ->
            var read: Int =-1
            val bytes = ByteArray(1024)
            while(fileInputStream.read(bytes).also{read = it} != -1){
                outputStream.write(bytes,0,read)
            }
        }
        return file.readBytes()
    }

    fun registerJournal(journal: Journal) {
        viewModelScope.launch {
            lateinit var registerJournal: JournalResponse
            withContext(Dispatchers.IO) {
                runCatching {
                    registerJournal = ServerRepository.registerJournal(journal)
                }.onFailure {
                    Log.e(this::class.java.simpleName, it.stackTraceToString())
                    withContext(Dispatchers.Main) {
                        isJournalSubmit.value = false
                    }
                }.onSuccess {
                    withContext(Dispatchers.Main) {
                        isJournalSubmit.value = true
                    }
                }
            }
        }
    }

    fun isJournalExists(timeStamp: Long) {
        viewModelScope.launch {
            val journal: Journal? = runCatching {
                withContext(Dispatchers.IO) {
                    ServerRepository.getJournal(timeStamp)
                }
            }.onFailure {
                Log.e(this::class.java.simpleName, "Error: ${it.stackTraceToString()}")
            }.getOrNull()

            withContext(Dispatchers.Main) {
                isJournalExistsByTimeStamp.value = journal
            }
        }

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
}