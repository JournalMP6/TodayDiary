package com.mptsix.todaydiary.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.databinding.FragmentDiaryBinding
import com.mptsix.todaydiary.viewmodel.JournalViewModel

class DiaryFragment : Fragment() {
    // Passed
    private var journalTimeStamp: Long? = null

    // Is Diary Exists?
    private var isDiaryExists: Boolean = false

    // For View Binding
    private var _fragmentDiaryBinding: FragmentDiaryBinding? = null
    private val fragmentDiaryBinding: FragmentDiaryBinding get() = _fragmentDiaryBinding!!

    // ViewModel
    private val journalViewModel: JournalViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Get Argument
        journalTimeStamp = arguments?.getLong("timeStamp")
        _fragmentDiaryBinding = FragmentDiaryBinding.inflate(inflater, container, false)
        return fragmentDiaryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateButtonVisibility()
        initObserver()
        // Check Journal Exists
        journalViewModel.isJournalExists(journalTimeStamp!!)
        init()
    }

    private fun init() {
        fragmentDiaryBinding.addDiary.setOnClickListener {
            journalViewModel.requestEditPage(journalTimeStamp!!)
        }
        fragmentDiaryBinding.modifyBtn.setOnClickListener {
            journalViewModel.requestEditPage(journalTimeStamp!!)
        }
    }

    private fun updateButtonVisibility() {
        if (!isDiaryExists) {
            fragmentDiaryBinding.modifyBtn.visibility = View.GONE
        } else {
            fragmentDiaryBinding.addDiary.visibility = View.GONE
        }
    }

    private fun initObserver() {
        journalViewModel.isJournalExistsByTimeStamp.observe(viewLifecycleOwner) {
            Log.e(this::class.java.simpleName, "Journal Exists: $it")
            isDiaryExists = it
        }
    }
}