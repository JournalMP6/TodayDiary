package com.mptsix.todaydiary.view.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.databinding.FragmentMainBinding
import com.mptsix.todaydiary.viewmodel.JournalViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment @Inject constructor(): SuperFragment<FragmentMainBinding>() {
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
            var now = System.currentTimeMillis()
            val currentTimeStamp: Long = simpleDateFormat.parse("${year}-${month+1}-$dayOfMonth").time
            if (now < currentTimeStamp){
                Toast.makeText(activity, "금일 이후의 일기는 작성할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }else{
                journalViewModel.requestDiaryPage(currentTimeStamp)
            }
        }

        /*binding.userInfoBtn.setOnClickListener {
            journalViewModel.requestUserInfoPage()
        }
        binding.userSearchBtn.setOnClickListener {
            journalViewModel.requestUserSearchPage()
        }*/
    }
}