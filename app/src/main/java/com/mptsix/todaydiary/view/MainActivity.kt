package com.mptsix.todaydiary.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mptsix.todaydiary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var getYear:String
    lateinit var getMonth:String
    lateinit var getDayOfMonth:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            getYear = year.toString()
            getMonth = (month+1).toString()
            getDayOfMonth = dayOfMonth.toString()

            intent = Intent(this, DiaryActivity::class.java)
            startActivity(intent)
            Toast.makeText(applicationContext, getYear + "/" + getMonth + "/" + getDayOfMonth, Toast.LENGTH_SHORT).show()
        }
    }

}