package com.mptsix.todaydiary.viewmodel

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mptsix.todaydiary.data.request.JournalDto
import com.mptsix.todaydiary.data.response.JournalResponse
import com.mptsix.todaydiary.model.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class JournalViewModel :ViewModel(){
    var isJournalSubmit : MutableLiveData<Boolean> = MutableLiveData()
    var isJournalEdited : MutableLiveData<Boolean> = MutableLiveData()

    fun getMultipartBody(uri: Uri, context:Context, contentResolver: ContentResolver): MultipartBody.Part{
        val file: File = File.createTempFile("SOME_RANDOM_IMAGE",null,context.cacheDir)
        val fileInputStream: InputStream = contentResolver.openInputStream(uri)!!

        FileOutputStream(file).use { outputStream ->
            var read: Int =-1
            val bytes = ByteArray(1024)
            while(fileInputStream.read(bytes).also{read = it} != -1){
                outputStream.write(bytes,0,read)
            }
        }
        //Request body
        val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file)
        //val requestBody = file.asRequestBody("multipart/form~data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("uploadFile",file.name,requestBody)
    }

    fun registerJournal(journalDto: JournalDto, picture: MultipartBody.Part?){
        viewModelScope.launch{
            lateinit var registerJournal: JournalResponse
            withContext(Dispatchers.IO){
                runCatching {
                    registerJournal = ServerRepository.registerJournal(journalDto) // 글
                    picture?.let {
                        ServerRepository.registerPicture(picture, journalDto)
                    }
                }.onFailure {
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

    fun editJournal(journalDto: JournalDto) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                runCatching {
                    ServerRepository.editJournal(journalDto)
                }.onSuccess {
                    isJournalEdited.value = true
                }.onFailure {
                    isJournalEdited.value = false
                }
            }
        }
    }

//    fun getPictureInfo(journalDate: Long, multiPartBody:MultipartBody.Part){
//        lateinit var savePicture : JournalRegisterPicture
//        savePicture.journalDate = journalDate
//        savePicture.multiPartBody = multiPartBody
//    }
}