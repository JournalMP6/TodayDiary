package com.example.mp6

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mp6.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.joinBtn.setOnClickListener {
            intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }// 회원가입 버튼 클릭 시, 회원가입 페이지로 전환

        binding.loginBtn.setOnClickListener {
            var userId :String = binding.inputLoginID.text.toString()
            var userPassword :String = binding.inputLoginPwd.text.toString()
            //*****DB와 맞는 정보 입력 시, 페이지 전환, 아닐 시 오류 메세지를 띄워야함, 정보 확인하는 로직 필요*****
            if(checkInput(userId, userPassword)){
                intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(applicationContext, "입력한 로그인 정보가 잘못 되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }// 로그인 버튼 클릭 시, 담겨져 있는 정보를 가져옴
    }

    fun checkInput(userId:String, userPassword:String):Boolean{
        return true
    }
}