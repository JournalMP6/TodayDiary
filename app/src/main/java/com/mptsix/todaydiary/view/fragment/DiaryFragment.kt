package com.mptsix.todaydiary.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.data.internal.DiaryWriteMode
import com.mptsix.todaydiary.data.response.Journal
import com.mptsix.todaydiary.databinding.FragmentDiaryBinding
import com.mptsix.todaydiary.viewmodel.JournalViewModel
import javax.xml.bind.DatatypeConverter

class DiaryFragment : Fragment() {
    // Passed
    private var journalTimeStamp: Long? = null

    // Is Diary Exists?
    private var journal: Journal? = null

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
            journalViewModel.journalCache = null
            journalViewModel.requestEditPage(DiaryWriteMode(journalTimeStamp!!, true))
        }
        fragmentDiaryBinding.modifyBtn.setOnClickListener {
            journalViewModel.journalCache = journal
            journalViewModel.requestEditPage(DiaryWriteMode(journalTimeStamp!!, false))
        }
    }

    private fun updateButtonVisibility() {
        if (journal == null) {
            fragmentDiaryBinding.mainDiaryViewLayout.visibility = View.GONE
            fragmentDiaryBinding.modifyBtn.visibility = View.GONE
            fragmentDiaryBinding.diaryPicture.visibility = View.GONE
            fragmentDiaryBinding.addDiary.visibility = View.VISIBLE
        } else {
            fragmentDiaryBinding.mainDiaryViewLayout.visibility = View.VISIBLE
            fragmentDiaryBinding.diaryPicture.visibility = View.GONE
            fragmentDiaryBinding.addDiary.visibility = View.GONE
            fragmentDiaryBinding.modifyBtn.visibility = View.VISIBLE
            showDiary()
        }
    }

    private fun initObserver() {
        journalViewModel.isJournalExistsByTimeStamp.observe(viewLifecycleOwner) {
            Log.e(this::class.java.simpleName, "Journal Exists: ${(journal != null)}")
            journal = it
            updateButtonVisibility()
        }
    }

    private fun showDiary() {
        journal?.let {
            fragmentDiaryBinding.diaryCategoryView.text = it.journalCategory.name
            fragmentDiaryBinding.weatherView.text = it.journalWeather
            fragmentDiaryBinding.locationView.text = it.journalLocation
            fragmentDiaryBinding.diaryMainContent.text = it.mainJournalContent
            if (it.journalImage.imageFile != null) {
                val decodedArray: ByteArray = DatatypeConverter.parseBase64Binary(it.journalImage.imageFile)
                val bitmapTmp: Bitmap = BitmapFactory.decodeByteArray(decodedArray, 0, decodedArray.size)
                fragmentDiaryBinding.diaryPicture.setImageBitmap(bitmapTmp)
            }
        }
    }
}