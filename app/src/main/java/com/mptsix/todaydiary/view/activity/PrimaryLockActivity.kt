package com.mptsix.todaydiary.view.activity

import android.app.Activity
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import com.mptsix.todaydiary.databinding.ActivityPrimaryLockBinding
import com.mptsix.todaydiary.viewmodel.LockViewModel

class PrimaryLockActivity : SuperActivity<ActivityPrimaryLockBinding>() {
    private val lockViewModel: LockViewModel by viewModels()
    override fun getViewBinding(): ActivityPrimaryLockBinding = ActivityPrimaryLockBinding.inflate(layoutInflater)
    var password:String = ""
    override fun initView() {
        btnClicked()
        submitPwd()
    }

    private fun starTextInit(){
        var star4TextView : String = ""
        for(times in 1..password.length){
            star4TextView += "* "
        }
        binding.secondaryPwd.text = star4TextView
    }

    private fun submitPwd(){
        val error:(t : Throwable) ->Unit = {
            Toast.makeText(this, "비밀번호 입력에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
        binding.submitBtn.setOnClickListener {
            lockViewModel.registerAuxiliaryPassword(password, error)
            finish()
        } // 서버로 비밀번호 전송
    }

    private fun btnClicked(){
        binding.apply {
            var btnList = listOf<Button>(oneBtn, twoBtn, threeBtn, fourBtn, fiveBtn,sixBtn,sevenBtn,eightBtn,nineBtn,starBtn, zeroBtn, sharpBtn)
            for(btn in btnList){
                btn.setOnClickListener {
                    writePwd(btn)
                }
            }
        }
    }

    private fun writePwd(button: Button){
        password += button.text.toString()
        starTextInit()
    }
}