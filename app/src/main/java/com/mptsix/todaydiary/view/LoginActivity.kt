package com.mptsix.todaydiary.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mptsix.todaydiary.data.request.LoginRequest
import com.mptsix.todaydiary.databinding.ActivityLoginBinding
import com.mptsix.todaydiary.viewmodel.LogInViewModel

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
            logInViewModel.login(LoginRequest(userId, userPassword))
        }// 로그인 버튼 클릭 시, 담겨져 있는 정보를 가져옴
    }

    private fun initObserver() {
        logInViewModel.loginSuccess.observe(this) {
            if (!it) {
                // Login Failed
                Toast.makeText(applicationContext, "입력한 로그인 정보가 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                // Login Succeed
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}