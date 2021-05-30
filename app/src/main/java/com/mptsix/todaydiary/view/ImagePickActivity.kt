package com.mptsix.todaydiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.ActivityImagePickBinding

class ImagePickActivity : AppCompatActivity() {
    lateinit var binding:ActivityImagePickBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePickBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}