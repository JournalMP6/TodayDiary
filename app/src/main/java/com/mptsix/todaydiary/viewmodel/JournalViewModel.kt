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
import com.mptsix.todaydiary.data.request.JournalRegisterPicture
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
        var file: File = File.createTempFile("SOME_RANDOM_IMAGE",null,context.cacheDir)
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

    fun registerJournal(journalDto: JournalDto, journalPicture: JournalRegisterPicture){
        //글만 쓰는 경우
        if(journalDto!=null && journalPicture==null){
            viewModelScope.launch{
                lateinit var registerJournal: JournalResponse
                withContext(Dispatchers.IO){
                    registerJournal = ServerRepository.registerJournal(journalDto)
                }
                withContext(Dispatchers.Main){
                    isJournalSubmit.value = (/*model과 서버 수행 결과 값*/)
                }
            }
        }
        //글과 사진 둘다 존재하는 경우
        else if(journalDto!=null && journalPicture!=null){
            viewModelScope.launch {
                lateinit var registerJournal: JournalResponse
                lateinit var registerPicture: PictureResponse
                withContext(Dispatchers.IO){
                    registerJournal = ServerRepository.registerJournal(journalDto)
                    registerPicture = ServerRepository.registerPicture(this, journalPicture)//uri 정보 어디서?
                }
                withContext(Dispatchers.Main){
                    //isJournalSubmit.value = (registerJournal.registeredJournal == journalDto.mainJournalContent)
                    isJournalSubmit.value = (/*model과 server 수행 결과 값*/)
                }
            }
        }else{//입력 값이 없거나 오류 발생 시
            Log.e(this::class.java.simpleName,"Journal register failed")
            isJournalSubmit.value = false
            return
        }
    }

    fun editJournal(journalDto: JournalDto) {
        //getJournal()의 반환형이 List....작성된 journal 다 반환?
        var targetJournal: List<JournalDto> =
            ServerRepository.getJournal() // targetJournal = 수정 대상 journal
        if (journalDto.mainJournalContent.length >= 10){
            viewModelScope.launch {
                lateinit var editedJournal: JournalResponse
                withContext(Dispatchers.IO) {
                    targetJournal.get(0).mainJournalContent = journalDto.mainJournalContent
                }
                withContext(Dispatchers.Main) {
                    isJournalEdited.value = (/*model과 server 수행 결과 값*/)
                }

            }
        }else{
            Log.e(this::class.java.simpleName, "Journal edited failed")
            isJournalEdited.value = false
            return
        }
    }

//    fun getPictureInfo(journalDate: Long, multiPartBody:MultipartBody.Part){
//        lateinit var savePicture : JournalRegisterPicture
//        savePicture.journalDate = journalDate
//        savePicture.multiPartBody = multiPartBody
//    }
}