package com.mptsix.todaydiary.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.databinding.ActivityLoginBinding
import com.mptsix.todaydiary.viewmodel.LogInViewModel
import java.lang.RuntimeException
import java.net.ConnectException

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    private val logInViewModel: LogInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initObserver()
    }

    private fun init() {
        binding.joinBtn.setOnClickListener {
            intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }// 회원가입 버튼 클릭 시, 회원가입 페이지로 전환

        binding.loginBtn.setOnClickListener {
            val userId :String = binding.inputLoginID.text.toString()
            val userPassword :String = binding.inputLoginPwd.text.toString()
            logInViewModel.login(LoginRequest(userId, userPassword), { t:Throwable->

                if(t is ConnectException){
                    Toast.makeText(this, "서버가 불안정합니다.\n잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
                if(t is RuntimeException){
                    Toast.makeText(this, "아이디 혹은 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                    binding.inputLoginID.text= null
                    binding.inputLoginPwd.text = null
                }


            })
        }// 로그인 버튼 클릭 시, 담겨져 있는 정보를 가져옴
    }

    private fun initObserver() {
        logInViewModel.loginSuccess.observe(this) {
            if(it) {
                // Login Succeed
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}