package com.mptsix.todaydiary.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.ActivityMainBinding
import com.mptsix.todaydiary.transition.DisplayTransition
import com.mptsix.todaydiary.view.fragment.DiaryFragment
import com.mptsix.todaydiary.view.fragment.MainFragment
import com.mptsix.todaydiary.viewmodel.JournalViewModel

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val journalViewModel: JournalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObserver()

        // Start with Main Fragment
        commitFragment(MainFragment())
    }

    private fun initObserver() {
        journalViewModel.displayTransition.observe(this) {
            when (it.displayTransition) {
                DisplayTransition.REQUEST_DIARY -> {
                    val bundle: Bundle = Bundle().apply {putLong("timeStamp", it.data as Long)}
                    val diaryFragment: DiaryFragment = DiaryFragment().apply {
                        arguments = bundle
                    }
                    commitFragment(diaryFragment)
                }
            }
        }
    }

    private fun commitFragment(targetFragment: Fragment, replace: Boolean = false) {
        supportFragmentManager.beginTransaction().apply {
            if (replace) addToBackStack(null)
            replace(R.id.mainViewContainer, targetFragment)
            commit()
        }
    }
}