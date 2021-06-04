package com.mptsix.todaydiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.ActivityPwdChangeBinding

class PwdChangeActivity : AppCompatActivity() {
    lateinit var binding :ActivityPwdChangeBinding
    //private val ProfileViewModel:ViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPwdChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        submit()
    }

    private fun pwdChcek(){
        //if (binding.presentPwd ==) -> 현재 비밀번호와 서버에서 받아온 비밀번호가 일치하는지 확인

        if(binding.changePwd.text.toString() == binding.checkPwd.text.toString()){// 변경할 비밀번호와 비밀번호 확인이 같은지 확인
            finish()
        }else{
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
        }
    }

    private fun submit(){
        binding.submitBtn.setOnClickListener {
            pwdChcek() // 버튼을 누르면 비밀번호가 올바르게 입력되었는지 확인
            binding.changePwd.text // 변경할 비밀번호
        }//변경할 비밀번호를 서버로 전송
    }
}