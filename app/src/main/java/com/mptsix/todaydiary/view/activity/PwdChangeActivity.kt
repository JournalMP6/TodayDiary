package com.mptsix.todaydiary.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.mptsix.todaydiary.data.internal.PasswordChangeRequest
import com.mptsix.todaydiary.databinding.ActivityPwdChangeBinding
import com.mptsix.todaydiary.viewmodel.ProfileViewModel

class PwdChangeActivity : SuperActivity<ActivityPwdChangeBinding>() {
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun getViewBinding(): ActivityPwdChangeBinding = ActivityPwdChangeBinding.inflate(layoutInflater)
    override fun initView() {
        submit()

        profileViewModel.isPasswordChangeSucceed.observe(this) {
            if (it) {
                Toast.makeText(this, "계정의 비밀번호가 성공적으로 바뀌었습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT)
                    .show()
                finishAffinity()
                intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)// need to check
            } else {
                Toast.makeText(this, "비밀번호를 바꾸는 데 실패했습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun pwdChcek(){
        //if (binding.presentPwd ==) -> 현재 비밀번호와 서버에서 받아온 비밀번호가 일치하는지 확인

        if(binding.changePwd.text.toString() != binding.checkPwd.text.toString()){// 변경할 비밀번호와 비밀번호 확인이 같은지 확인
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
        }
    }
    private fun checkTextBlank(presentPwd:String, changePwd:String):Boolean{
        if(presentPwd.isEmpty() || changePwd.isEmpty()){
            return false
        }
        return true
    }

    private fun submit(){
        var presentPwd:String = binding.presentPwd.text.toString()
        var changePwd:String = binding.changePwd.text.toString()
        binding.submitBtn.setOnClickListener {
            pwdChcek() // 버튼을 누르면 비밀번호가 올바르게 입력되었는지 확인
            if(checkTextBlank(presentPwd,changePwd)){
                profileViewModel.changePassword(
                    PasswordChangeRequest(
                        changePwd
                    )
                )
            }else{
                Toast.makeText(this, "Password is Empty.Try it again.", Toast.LENGTH_SHORT).show()
                submit()
            }

        }//변경할 비밀번호를 서버로 전송
    }
}