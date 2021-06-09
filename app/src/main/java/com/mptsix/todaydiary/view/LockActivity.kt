package com.mptsix.todaydiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mptsix.todaydiary.databinding.ActivityLockBinding

class LockActivity : AppCompatActivity() {
    lateinit var binding : ActivityLockBinding
    var password:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLockBinding.inflate(layoutInflater)
        setContentView(binding.root)
        btnClicked()
        submitPwd()
    }

    override fun onBackPressed() {
    }// 다 같은 구성인데, 이 함수만 차이가 남. 조건적으로 override 할 수 있는 방법이 있는지,,

    private fun observePwd(){

    }// 잠금화면 비밀번호가 있는지 없는지를 확인해서 없다면 서버에 비밀번호 등록
    // 있다면 비밀번호가 일치하는 지 확인 하는 방식이 좋을 거 같다고 생각합니다.

    private fun starTextInit(){
        var star4TextView : String = ""
        for(times in 1..password.length){
            star4TextView += "*"
        }
        binding.secondaryPwd.text = star4TextView
    }

    private fun submitPwd(){
        binding.submitBtn.setOnClickListener {

        } // 서버로 비밀번호 전송
    }

    private fun btnClicked(){
        binding.apply {
            oneBtn.setOnClickListener {
                password += oneBtn.text.toString()
                starTextInit()
            }
            twoBtn.setOnClickListener {
                password += twoBtn.text.toString()
                starTextInit()
            }
            threeBtn.setOnClickListener {
                password += threeBtn.text.toString()
                starTextInit()
            }
            fourBtn.setOnClickListener {
                password += fourBtn.text.toString()
                starTextInit()
            }
            fiveBtn.setOnClickListener {
                password += fiveBtn.text.toString()
                starTextInit()
            }
            sixBtn.setOnClickListener {
                password += sixBtn.text.toString()
                starTextInit()
            }
            sevenBtn.setOnClickListener {
                password += sevenBtn.text.toString()
                starTextInit()
            }
            eightBtn.setOnClickListener {
                password += eightBtn.text.toString()
                starTextInit()
            }
            nineBtn.setOnClickListener {
                password += nineBtn.text.toString()
                starTextInit()
            }
            starBtn.setOnClickListener {
                password += starBtn.text.toString()
                starTextInit()
            }
            zeroBtn.setOnClickListener {
                password += zeroBtn.text.toString()
                starTextInit()
            }
            sharpBtn.setOnClickListener {
                password += sharpBtn.text.toString()
                starTextInit()
            }
        }
    }
}