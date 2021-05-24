package com.mptsix.todaydiary.view

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.data.request.UserRegisterRequest
import com.mptsix.todaydiary.databinding.ActivityJoinBinding
import com.mptsix.todaydiary.viewmodel.LogInViewModel

class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding

    private val logInViewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

            logInViewModel.register(
                UserRegisterRequest(
                    userId = userId,
                    userPassword = userPassword,
                    userName = userName,
                    userDateOfBirth = userDateOfBirth,
                    userPasswordQuestion = userPasswordQuestion,
                    userPasswordAnswer = userPasswordAnswer,
                )
            )
        }// 제출 버튼 클릭 시, 담겨져 있는 정보를 변수에 저장하고 현재 activity 종료, ****DB에 저장할 수 있게 변환 필요****
    }

    private fun initObserver() {
        logInViewModel.registerSuccess.observe(this) {
            if (!it) {
                // Register Failed
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            } else {
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