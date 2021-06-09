package com.mptsix.todaydiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
    }

    private fun observePwd(){

    }

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
            binding.apply {
                var btnList = listOf<Button>(
                    oneBtn,
                    twoBtn,
                    threeBtn,
                    fourBtn,
                    fiveBtn,
                    sixBtn,
                    sevenBtn,
                    eightBtn,
                    nineBtn,
                    starBtn,
                    zeroBtn,
                    sharpBtn
                )
                for (btn in btnList) {
                    btn.setOnClickListener {
                        writePwd(btn)
                    }
                }
            }
        }
    }
    private fun writePwd(button: Button){
        password += button.text.toString()
        starTextInit()
    }
}