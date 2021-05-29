package com.mptsix.todaydiary.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryBinding
    //lateinit var bodyText:Boolean -> 이 변수로 일기가 로딩되었는지 아닌지 판단

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {

        /*if(bodyText){
            bodyText == True -> 일기장 내용을 text
            binding.addDiary.visibility = View.GONE
        }else{
            bodyText == false -> "일기가 존재하지 않습니다."
            binding.modifyBtn.visibility = View.GONE
        }*/
        binding.addDiary.setOnClickListener {
            movePage()
        }
        binding.modifyBtn.setOnClickListener {
            movePage()
        }
    }

    private fun movePage(){
        intent = Intent(this, EditDiaryActivity::class.java)
        startActivity(intent)
    }
}