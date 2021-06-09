package com.mptsix.todaydiary.view.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.data.internal.DiaryWriteMode
import com.mptsix.todaydiary.data.response.Journal
import com.mptsix.todaydiary.databinding.FragmentDiaryBinding
import com.mptsix.todaydiary.viewmodel.JournalViewModel
import javax.xml.bind.DatatypeConverter

class DiaryFragment : SuperFragment<FragmentDiaryBinding>() {
    // Passed
    private var journalTimeStamp: Long? = null

    // Is Diary Exists?
    private var journal: Journal? = null

    // ViewModel
    private val journalViewModel: JournalViewModel by activityViewModels()

    // onCreateView
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentDiaryBinding {
        // Get Argument
        journalTimeStamp = arguments?.getLong("timeStamp")

        return FragmentDiaryBinding.inflate(inflater, container, false)
    }

    // onViewCreated
    override fun initView() {
        updateButtonVisibility()
        initObserver()
        // Check Journal Exists
        journalViewModel.isJournalExists(journalTimeStamp!!)
        init()
    }

    private fun init() {
        binding.addDiary.setOnClickListener {
            journalViewModel.journalCache = null
            journalViewModel.requestEditPage(DiaryWriteMode(journalTimeStamp!!, true))
        }
        binding.modifyBtn.setOnClickListener {
            journalViewModel.journalCache = journal
            journalViewModel.requestEditPage(DiaryWriteMode(journalTimeStamp!!, false))
        }
    }

    private fun updateButtonVisibility() {
        if (journal == null) {
            Log.d(this::class.java.simpleName, "Found NULL")
            binding.mainDiaryViewLayout.visibility = View.GONE
            binding.modifyBtn.visibility = View.GONE
            binding.diaryPicture.visibility = View.GONE
            binding.addDiary.visibility = View.VISIBLE
        } else {
            Log.d(this::class.java.simpleName, "Found Journal")
            binding.mainDiaryViewLayout.visibility = View.VISIBLE
            binding.diaryPicture.visibility = View.VISIBLE
            binding.addDiary.visibility = View.GONE
            binding.modifyBtn.visibility = View.VISIBLE
            showDiary()
        }
    }

    private fun initObserver() {
        journalViewModel.isJournalExistsByTimeStamp.observe(viewLifecycleOwner) {
            Log.d(this::class.java.simpleName, "Journal Exists: ${(it != null)}")
            journal = it
            updateButtonVisibility()
        }

        journalViewModel.journalLocation.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.results.isEmpty()) {
                    Log.e(this::class.java.simpleName, "Server Exchange succeed, but formatted address didn't received!")
                } else {
                    binding.locationView.text = it.results[0].formatted_address
                }
            } else {
                Log.e(this::class.java.simpleName, "Location responded with NULL!")
            }
        }
    }

    private fun showDiary() {
        journal?.let {
            journalViewModel.getLocationFromGeo(it.journalLocation)
            binding.diaryCategoryView.text = it.journalCategory.name
            binding.weatherView.text = it.journalWeather
            binding.locationView.text = ""
            binding.diaryMainContent.text = it.mainJournalContent
            if (it.journalImage.imageFile != null) {
                val decodedArray: ByteArray = DatatypeConverter.parseBase64Binary(it.journalImage.imageFile)
                val bitmapTmp: Bitmap = BitmapFactory.decodeByteArray(decodedArray, 0, decodedArray.size)
                binding.diaryPicture.setImageBitmap(bitmapTmp)
            }
        }
    }
}