package com.example.mp6

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.mp6.databinding.ActivityJoinBinding

class JoinActivity : AppCompatActivity() {
    lateinit var binding: ActivityJoinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        attachAdapter()
        init()
    }

    private fun init() {
        binding.submitBtn.setOnClickListener {
            var userId: String = binding.inputJoinID.text.toString()
            var userPassword: String = binding.inputJoinPwd.text.toString()
            var userName: String = binding.inputJoinName.text.toString()
            var userDateOfBirth: String = binding.inputJoinBirth.text.toString()
            var userPasswordQuestion: String = binding.pwdQuestionSpinner.selectedItem.toString()
            var userPasswordAnswer: String = binding.inputPwdAnswer.text.toString()
            finish()
        }// 제출 버튼 클릭 시, 담겨져 있는 정보를 변수에 저장하고 현재 activity 종료, ****DB에 저장할 수 있게 변환 필요****
    }

    fun attachAdapter(){
        val spinner:Spinner = binding.pwdQuestionSpinner
        val question = ArrayAdapter.createFromResource(this, R.array.passwordQuestion, R.layout.spinner_layout).apply {
            setDropDownViewResource(R.layout.spinner_layout)
        }

        spinner.adapter = question
    }// 스피너의 바뀐 layout으로 설정하는 adapter

}