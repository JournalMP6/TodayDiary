package com.mptsix.todaydiary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.ActivityEditDiaryBinding

class EditDiaryActivity : AppCompatActivity() {
    lateinit var binding : ActivityEditDiaryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        attachAdapter()
    }

    private fun attachAdapter(){
        val spinner: Spinner = binding.categorySpinner
        val category = ArrayAdapter.createFromResource(this,
            R.array.category,
            R.layout.spinner_layout
        ).apply {
            setDropDownViewResource(R.layout.spinner_layout)
        }
        spinner.adapter = category
    }
}