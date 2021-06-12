package com.mptsix.todaydiary.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.mptsix.todaydiary.data.login.LoginSessionRepository
import com.mptsix.todaydiary.databinding.ActivitySplashBinding
import com.mptsix.todaydiary.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@AndroidEntryPoint
class splashActivity @Inject constructor() : SuperActivity<ActivitySplashBinding>() {
    private val splashViewModel: SplashViewModel by viewModels()

    override fun getViewBinding(): ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
    override fun initView() {
        init()

        // Splash Activity is the first activity shown to user.
        LoginSessionRepository.initiateRepository(applicationContext)
        splashViewModel.isLoginRedirectionNeeded {
            when (it) {
                is ConnectException, is SocketTimeoutException -> Toast.makeText(this, "서버와 연결을 하지 못했습니다. 인터넷 연결을 확인하고 재로그인 해주세요.", Toast.LENGTH_SHORT).show()
                is RuntimeException -> Toast.makeText(this, "비밀번호가 외부에서 변경된 것 같습니다. 보안을 위해 자동 로그인 데이터를 모두 삭제합니다. 재로그인 해주세요.", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "알 수 없는 에러가 발생했습니다. 메시지: ${it.message}", Toast.LENGTH_SHORT).show()
            }

            // Remove all user data
            splashViewModel.removeAllSessionData()
        }
    }

    private fun init() {
        splashViewModel.isDirectedToLogin.observe(this) {
            if (it) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // login completed
                val targetIntent: Intent = Intent(this, MainActivity::class.java)
                startActivity(targetIntent)
                finish()
            }
        }
    }// splash 화면, 3초동안 어플 정보를 띄워주고 로그인 페이지로 이동
}