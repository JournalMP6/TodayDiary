package com.mptsix.todaydiary.model

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerRepository {
    private var instance:Retrofit ?= null
    private val gson = GsonBuilder().setLenient().create()
    private const val URL = "http://www.naver.com"

    public fun getInstance():Retrofit{
        if(instance == null) {
            instance = Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return instance!!
    }

//    private fun initApiInterface():ServerAPI{
//        return retroFit.create()
//    }
}