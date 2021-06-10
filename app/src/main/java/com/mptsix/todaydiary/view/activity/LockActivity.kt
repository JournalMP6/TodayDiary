package com.mptsix.todaydiary.view.activity

import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.observe
import com.mptsix.todaydiary.databinding.ActivityLockBinding
import com.mptsix.todaydiary.viewmodel.LockViewModel

class LockActivity : SuperActivity<ActivityLockBinding>() {
    private val lockViewModel: LockViewModel by viewModels()

    var password:String = ""
    override fun getViewBinding(): ActivityLockBinding = ActivityLockBinding.inflate(layoutInflater)
    override fun initView() {
        btnClicked()
        submitPwd()
        observePwd()
    }

    override fun onBackPressed() {
    }

    private fun observePwd(){
        lockViewModel.isCheckSucceeds.observe(this){
            if(it){
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                // when password not match password erase
                password = ""
                binding.secondaryPwd.text = null
            }
        }
    }

    private fun starTextInit(){
        var star4TextView : String = ""
        for(times in 1..password.length){
            star4TextView += "* "
        }
        binding.secondaryPwd.text = star4TextView
    }

    private fun submitPwd(){
        binding.submitBtn.setOnClickListener {
            lockViewModel.checkAuxiliaryPassword(password)
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