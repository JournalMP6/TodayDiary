package com.mptsix.todaydiary.view.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.databinding.FragmentMainBinding
import com.mptsix.todaydiary.viewmodel.JournalViewModel
import java.text.SimpleDateFormat

class MainFragment: SuperFragment<FragmentMainBinding>() {
    // Simple Date Formatter
    private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    // Journal View Model[Context-Shared]
    private val journalViewModel: JournalViewModel by activityViewModels()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val currentTimeStamp: Long = simpleDateFormat.parse("${year}-${month+1}-$dayOfMonth").time
            journalViewModel.requestDiaryPage(currentTimeStamp)
        }

        /*binding.userInfoBtn.setOnClickListener {
            journalViewModel.requestUserInfoPage()
        }
        binding.userSearchBtn.setOnClickListener {
            journalViewModel.requestUserSearchPage()
        }*/
    }
}