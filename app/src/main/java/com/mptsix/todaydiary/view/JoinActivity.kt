package com.mptsix.todaydiary.view

import android.content.Intent
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
                ,{ ->

                    showDialog("Invalid Error", "아이디 비밀번호 규칙이 지켜지지 않았습니다. \n규칙에 맞게 입력해주세요.\n규칙(아이디:이메일, 비밀번호:8자 이상)")
                    binding.inputJoinID.text = null
                    binding.inputJoinPwd.text = null
                },
                {t:Throwable ->
                    if(t is ConnectException){
                        showDialog("Server Error", "서버 상태가 불안정합니다. \n잠시 후에 다시 시도해주세요.")
                    }
                    else if(t is RuntimeException){
                        Toast.makeText(this, "이미 등록된 아이디 입니다. \n다른 아이디를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        binding.inputJoinID.text=null
                    }
                }
            )
        }// 제출 버튼 클릭 시, 담겨져 있는 정보를 변수에 저장하고 현재 activity 종료, ****DB에 저장할 수 있게 변환 필요****
    }

    private fun showDialog(title:String, message: String){
        val builder: AlertDialog.Builder? = this.let{
            AlertDialog.Builder(it)
        }
        builder?.setMessage(message)
            ?.setTitle(title)
            ?.setPositiveButton("확인"){
                    _, _ ->
            }

        val dialog:AlertDialog ?= builder?.create()
        dialog?.show()
    }

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