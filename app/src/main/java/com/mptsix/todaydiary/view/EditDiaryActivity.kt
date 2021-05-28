package com.mptsix.todaydiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.ActivityEditDiaryBinding

class EditDiaryActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}