package com.mptsix.todaydiary.viewmodel

import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LogInViewModel: ViewModel() {
    var LoginSuccess = MutableLiveData<Boolean>()
    var RegisterSuccess = MutableLiveData<Boolean>()
    lateinit var userLoginResponse: LoginResponse
    //login과 그 결과에 따른 LiveData에 정보 입력
    fun userLoginRequest(email:String, pw:String){
        var userLoginRequest= LoginRequest(email,pw)
        /*userLoginRequest.userId = email
        userLoginRequest.userPassword =pw*/
    }
    fun userLoginResponse(){
        var userLoginResponse:LoginResponse
        if(userLoginResponse.userToken=//success){
            LoginSuccess.value = true;
        else{
            LoginSuccess.value = false;
        }
    }

    //입력형식 정규식에 부합하는지 확인
    fun inputValidation(strId:String, strPassword:String): Boolean {
        var flag: Boolean = false
        if(Patterns.EMAIL_ADDRESS.matcher(strId).matches() && strPassword.length>8){
            flag=true
        }
        return flag
    }

    /*fun getUser():

    fun login(userLoginRequest: LoginRequest){
        var userId:String
        login 기능 fun명이 login이라 할때
        userLoginRequest.userId
    }

    fun onClick(view :View){
        LoginUser loginuser
    }*/

    fun register(userId:String, userPassword:String, userName:String, userDataOfBirth:String, userPasswordQuestion:String, userPasswordAnswer:String){
        val userRegi = UserRegisterRequest(userId, userPassword,userName,userDataOfBirth,userPasswordQuestion,userPasswordAnswer)
    }
    fun registerResponse(){
        val userRegisterResponse = UserRegisterResponse()
        if(userRegisterResponse.registeredId == null)
            RegisterSuccess.value =false
        else
            RegisterSuccess.value = true
    }
}
