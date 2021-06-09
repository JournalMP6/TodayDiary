package com.mptsix.todaydiary.view.activity

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
import java.net.SocketTimeoutException

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

            if(userId.isNotEmpty() && userPassword.isNotEmpty()){
                logInViewModel.login(LoginRequest(userId, userPassword)) { t: Throwable ->
                    when (t) {
                        is ConnectException, is SocketTimeoutException -> Toast.makeText(this, "서버가 불안정합니다.\n잠시 후에 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                        is RuntimeException -> {
                            Toast.makeText(this, "아이디 혹은 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                            binding.inputLoginID.text = null
                            binding.inputLoginPwd.text = null
                        }
                        else -> {
                            Toast.makeText(this, "알 수 없는 에러가 발생했습니다. 메시지: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }else{
                Log.d("Test",  "Data is empty")
                Toast.makeText(this, "Data is empty. Please fill it.", Toast.LENGTH_SHORT).show()
                init()
            }// need to check
        }// 로그인 버튼 클릭 시, 담겨져 있는 정보를 가져옴
    }

    private fun initObserver() {
        logInViewModel.loginSuccess.observe(this) {
            if(it) {
                // Register ID to DB
                if (binding.autoLoginEnable.isChecked) {
                    Log.d(this::class.java.simpleName, "Autologin is enabled.")
                    logInViewModel.registerIdToDb(
                        userID = binding.inputLoginID.text.toString(),
                        userPassword = binding.inputLoginPwd.text.toString()
                    )
                } else {
                    Log.d(this::class.java.simpleName, "Autologin is disabled.")
                    logInViewModel.removeAllSession()
                }

                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}