package com.mptsix.todaydiary.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.Fragment
import com.mptsix.todaydiary.databinding.FragmentMainBinding
import com.mptsix.todaydiary.viewmodel.JournalViewModel
import java.text.SimpleDateFormat

class MainFragment: Fragment() {
    // For View Binding
    private var _fragmentMainBinding: FragmentMainBinding? = null
    private val fragmentMainBinding: FragmentMainBinding get() = _fragmentMainBinding!!

    // Simple Date Formatter
    private val simpleDateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")

    // Journal View Model[Context-Shared]
    private val journalViewModel: JournalViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentMainBinding = FragmentMainBinding.inflate(inflater, container, false)
        return fragmentMainBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentMainBinding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val currentTimeStamp: Long = simpleDateFormat.parse("${year}-${month+1}-$dayOfMonth").time
            journalViewModel.requestDiaryPage(currentTimeStamp)
        }

        fragmentMainBinding.userInfoBtn.setOnClickListener {
            journalViewModel.requestUserInfoPage()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        // Dereference variable so GC can free its memory
        _fragmentMainBinding = null
    }
}