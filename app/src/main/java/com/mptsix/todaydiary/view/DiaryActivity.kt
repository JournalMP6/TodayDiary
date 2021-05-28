package com.mptsix.todaydiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.ActivityDiaryBinding

class DiaryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}