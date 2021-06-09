package com.mptsix.todaydiary.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.mptsix.todaydiary.data.login.LoginSessionRepository
import com.mptsix.todaydiary.databinding.ActivitySplashBinding

class splashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LoginSessionRepository.initiateRepository(applicationContext)
        init()
    }

    private fun init() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }// splash 화면, 3초동안 어플 정보를 띄워주고 로그인 페이지로 이동
}