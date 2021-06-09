package com.mptsix.todaydiary.view.activity

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.databinding.ActivityJoinBinding
import com.mptsix.todaydiary.viewmodel.LogInViewModel
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException

class JoinActivity : SuperActivity<ActivityJoinBinding>() {
    private val logInViewModel: LogInViewModel by viewModels()

    override fun getViewBinding(): ActivityJoinBinding = ActivityJoinBinding.inflate(layoutInflater)
    override fun initView() {
        attachAdapter()
        init()
        initObserver()
    }

    private fun init() {
        binding.submitBtn.setOnClickListener {
            val userId: String = binding.inputJoinID.text.toString()
            val userPassword: String = binding.inputJoinPwd.text.toString()
            val userName: String = binding.inputJoinName.text.toString()
            val userDateOfBirth: String = binding.inputJoinBirth.text.toString()
            val userPasswordQuestion: String = binding.pwdQuestionSpinner.selectedItem.toString()
            val userPasswordAnswer: String = binding.inputPwdAnswer.text.toString()

            val userData = listOf<String>(userId, userName, userPassword, userDateOfBirth, userPasswordAnswer, userPasswordQuestion)

            if (checkTextBlank(userData)) {
                logInViewModel.register(
                    userRegisterRequest = UserRegisterRequest(
                        userId = userId,
                        userPassword = userPassword,
                        userName = userName,
                        userDateOfBirth = userDateOfBirth,
                        userPasswordQuestion = userPasswordQuestion,
                        userPasswordAnswer = userPasswordAnswer,
                    ),
                    _invalidFailure = {
                        showDialog(this,"Invalid Error", "아이디 비밀번호 규칙이 지켜지지 않았습니다. \n규칙에 맞게 입력해주세요.\n규칙(아이디:이메일, 비밀번호:8자 이상)")
                        binding.inputJoinID.text = null
                        binding.inputJoinPwd.text = null
                    },
                    _onFailure = {
                        when (it) {
                            is ConnectException, is SocketTimeoutException -> showDialog(this, "Server Error", "서버 상태가 불안정합니다. \n잠시 후에 다시 시도해주세요.")
                            is RuntimeException -> {
                                Toast.makeText(this, "이미 등록된 아이디 입니다. \n다른 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                                binding.inputJoinID.text=null
                            }
                            else -> {
                                Toast.makeText(this, "알 수 없는 에러가 발생했습니다. 메시지: ${it.message}", Toast.LENGTH_SHORT).show()
                                binding.inputJoinID.text = null
                            }
                        }
                    }
                )
            } else {
                init()
            }
        }// 제출 버튼 클릭 시, 담겨져 있는 정보를 변수에 저장하고 현재 activity 종료, ****DB에 저장할 수 있게 변환 필요****
    }

    private fun checkTextBlank(userData : List<String>):Boolean{
        for(data in userData){
            if(data.isEmpty()){
                Log.d("Test",  "Data is empty")
                Toast.makeText(this, "Data is empty. Please fill it.", Toast.LENGTH_SHORT).show()
                return false
            }else{
                continue
            }
        }
        return true
    }// 입력한 내용이 비어있는지 확인


    private fun initObserver() {
        logInViewModel.registerSuccess.observe(this) {
            if(it) {
                // Register Succeed
                finish()
            }
        }
    }

    private fun attachAdapter(){
        val spinner:Spinner = binding.pwdQuestionSpinner
        val question = ArrayAdapter.createFromResource(this,
            R.array.passwordQuestion,
            R.layout.spinner_layout
        ).apply {
            setDropDownViewResource(R.layout.spinner_layout)
        }

        spinner.adapter = question
    }// 스피너의 바뀐 layout으로 설정하는 adapter
}