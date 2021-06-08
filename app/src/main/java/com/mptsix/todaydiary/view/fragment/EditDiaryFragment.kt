package com.mptsix.todaydiary.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.data.response.Journal
import com.mptsix.todaydiary.data.response.JournalCategory
import com.mptsix.todaydiary.data.response.JournalImage
import com.mptsix.todaydiary.databinding.FragmentEditDiaryBinding
import com.mptsix.todaydiary.view.MapActivity
import com.mptsix.todaydiary.viewmodel.JournalViewModel

import javax.xml.bind.DatatypeConverter

class EditDiaryFragment : Fragment() {
    private var _fragmentEditDiaryBinding: FragmentEditDiaryBinding? = null
    private val fragmentEditDiaryBinding: FragmentEditDiaryBinding get() = _fragmentEditDiaryBinding!!

    // Diary Target Date
    private var journalTimeStamp: Long? = null
    private var journalWriteMode: Boolean = false // true for write, false for edit

    private val journalViewModel: JournalViewModel by activityViewModels()

    // Data
    private var journalImage: JournalImage? = null

    // Location
    private var journalLocation: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        journalTimeStamp = arguments?.getLong("timeStamp")
        journalWriteMode = arguments?.getBoolean("modeType") ?: false
        Log.d(this::class.java.simpleName, "Target Time Stamp: $journalTimeStamp")
        Log.d(this::class.java.simpleName, "Target Mode: $journalWriteMode")
        _fragmentEditDiaryBinding = FragmentEditDiaryBinding.inflate(inflater, container, false)
        return fragmentEditDiaryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachAdapter()
        init()
        journalViewModel.isJournalSubmit.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Submit: $it", Toast.LENGTH_LONG).show()
        }
        applyMode()
        //getLocation()
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _fragmentEditDiaryBinding = null
    }

    private fun applyMode() {
        if (!journalWriteMode) {
            journalViewModel.journalCache?.let {
                fragmentEditDiaryBinding.diaryBody.setText(it.mainJournalContent)
            }
        }
        fragmentEditDiaryBinding.editText4.isEnabled = journalWriteMode
        fragmentEditDiaryBinding.categorySpinner.isEnabled = journalWriteMode
        fragmentEditDiaryBinding.getLocationBtn.isEnabled = journalWriteMode
        fragmentEditDiaryBinding.getImageBtn.isEnabled = journalWriteMode
        fragmentEditDiaryBinding.weatherSpinner.isEnabled = journalWriteMode
        fragmentEditDiaryBinding.diaryBody.isEnabled = true
    }

    private fun init() {
        lateinit var intent: Intent
        fragmentEditDiaryBinding.getLocationBtn.setOnClickListener {
            intent = Intent(activity, MapActivity::class.java)
            startActivityForResult(intent,0)
        }// 버튼 클릭 시 MapActivity 호출
        fragmentEditDiaryBinding.getImageBtn.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }// 버튼 클릭 시 ImageActivity 호출
        fragmentEditDiaryBinding.submitBtn.setOnClickListener {
            val journal: Journal = if (!journalWriteMode) {
                journalViewModel.journalCache!!.mainJournalContent = fragmentEditDiaryBinding.diaryBody.text.toString()
                journalViewModel.journalCache!!
            } else {
                // Category
                val journalCategory: JournalCategory = categorySelectionToEnum(
                    fragmentEditDiaryBinding.categorySpinner.selectedItem.toString()
                )

                // Diary
                val journalWeather: String =
                    fragmentEditDiaryBinding.weatherSpinner.selectedItem.toString()

                Journal(
                    mainJournalContent = fragmentEditDiaryBinding.diaryBody.text.toString(),
                    journalLocation = journalLocation, // TODO: For now, just set to test
                    journalCategory = journalCategory,
                    journalWeather = journalWeather,
                    journalDate = journalTimeStamp!!,
                    journalImage = JournalImage(
                        journalImage?.imageFile
                    )
                )
            }
            journalViewModel.registerJournal(journal)
        }

    }

    private fun attachAdapter() {
        val categorySpinner: Spinner = fragmentEditDiaryBinding.categorySpinner
        val category = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category,
            R.layout.spinner_layout
        ).apply {
            setDropDownViewResource(R.layout.spinner_layout)
        }
        val weatherSpinner: Spinner = fragmentEditDiaryBinding.weatherSpinner
        val weather = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.weather,
            R.layout.spinner_layout
        ).apply {
            setDropDownViewResource(R.layout.spinner_layout)
        }
        categorySpinner.adapter = category
        weatherSpinner.adapter = weather
    }

    private fun categorySelectionToEnum(selection: String): JournalCategory {
        return when (selection) {
            JournalCategory.ACHIEVEMENT_JOURNAL.name -> JournalCategory.ACHIEVEMENT_JOURNAL
            JournalCategory.THANKS_JOURNAL.name -> JournalCategory.THANKS_JOURNAL
            JournalCategory.EMOTIONAL_JOURNAL.name -> JournalCategory.EMOTIONAL_JOURNAL
            else -> JournalCategory.EMOTIONAL_JOURNAL
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                journalLocation = data!!.action!!
            }else{
                Toast.makeText(requireContext(), "fail",Toast.LENGTH_SHORT).show()
            }
        }else if(requestCode == 1){
            if (resultCode == Activity.RESULT_OK) {
                val photoUri:Uri? = data?.data
                journalImage = JournalImage(
                    DatatypeConverter.printBase64Binary(journalViewModel.getByteArray(photoUri!!, requireContext()))
                )
            }
        }
    }
}