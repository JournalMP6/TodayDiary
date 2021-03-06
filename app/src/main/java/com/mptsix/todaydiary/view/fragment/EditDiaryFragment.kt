package com.mptsix.todaydiary.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.data.internal.UserSealed
import com.mptsix.todaydiary.data.response.Journal
import com.mptsix.todaydiary.data.response.JournalCategory
import com.mptsix.todaydiary.data.response.JournalImage
import com.mptsix.todaydiary.data.temp.TempJournal
import com.mptsix.todaydiary.databinding.FragmentEditDiaryBinding
import com.mptsix.todaydiary.view.activity.MapActivity
import com.mptsix.todaydiary.viewmodel.JournalViewModel
import com.mptsix.todaydiary.viewmodel.LockViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import javax.xml.bind.DatatypeConverter

@AndroidEntryPoint
class EditDiaryFragment @Inject constructor() : SuperFragment<FragmentEditDiaryBinding>() {
    // Diary Target Date
    var journalTimeStamp: Long? = null
        set(value) {
            Log.d(this::class.java.simpleName, "Setting Journal Time Stamp as: $value")
            field = value
        }
    var journalWriteMode: Boolean = false // true for write, false for edit
        set(value) {
            Log.d(this::class.java.simpleName, "Setting Journal Write mode as $value")
            field = value
        }

    private val journalViewModel: JournalViewModel by activityViewModels()
    private val lockViewModel: LockViewModel by activityViewModels()
    // Data
    private var journalImage: JournalImage? = null

    // Location
    private var journalLocation: String = ""

    private val category: ArrayAdapter<CharSequence> by lazy {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category,
            R.layout.spinner_layout
        ).apply {
            setDropDownViewResource(R.layout.spinner_layout)
        }
    }

    private val weather: ArrayAdapter<CharSequence> by lazy {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.weather,
            R.layout.spinner_layout
        ).apply {
            setDropDownViewResource(R.layout.spinner_layout)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditDiaryBinding {
        Log.d(this::class.java.simpleName, "Target Time Stamp: $journalTimeStamp")
        Log.d(this::class.java.simpleName, "Target Mode: $journalWriteMode")

        return FragmentEditDiaryBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        initObserver()
        journalViewModel.getUserSealed()

        attachAdapter()
        init()
        applyMode()

        checkTempData()
    }

    private fun initObserver() {
        journalViewModel.isJournalSubmit.observe(viewLifecycleOwner) {
            if(it) Toast.makeText(requireContext(), "Submit: $it", Toast.LENGTH_LONG).show()
            activity?.onBackPressed()
        }

        journalViewModel.tempJournal.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.diaryBody.setText(it.mainJournalContent)
                binding.editText4.setText(it.mainTitle)
                journalLocation = it.journalLocation
                journalImage = JournalImage(
                    imageFile = it.journalImage
                )

                // TODO: Spinner Initiation
                val categoryPosition: Int = category.getPosition(it.journalCategory)
                binding.categorySpinner.setSelection(categoryPosition)

                val weatherPosition: Int = weather.getPosition(it.journalWeather)
                binding.weatherSpinner.setSelection(weatherPosition)
            } else {
                binding.diaryBody.setText("")
                binding.editText4.setText("")
                journalLocation = ""
                journalImage = null

                // TODO: Spinner Initiation
                binding.categorySpinner.setSelection(0)
                binding.weatherSpinner.setSelection(0)
            }
        }
    }

    private fun checkTempData() {
        val userSealed: UserSealed = journalViewModel.getUserSealed() ?: return
        val targetDate: Long = journalTimeStamp ?: run {
            Log.w(this::class.java.simpleName, "Cannot get Journal timestamp")
            return
        }
        journalViewModel.getTempDataIfExists(targetDate, userSealed.userId)
    }

    private fun applyMode() {
        if (!journalWriteMode) {
            journalViewModel.journalCache?.let {
                binding.diaryBody.setText(it.mainJournalContent)
            }
        }
        binding.editText4.isEnabled = journalWriteMode
        binding.categorySpinner.isEnabled = journalWriteMode
        binding.getLocationBtn.isEnabled = journalWriteMode
        binding.getImageBtn.isEnabled = journalWriteMode
        binding.weatherSpinner.isEnabled = journalWriteMode
        binding.diaryBody.isEnabled = true
    }

    private fun init() {
        lateinit var intent: Intent
        binding.getLocationBtn.setOnClickListener {
            intent = Intent(activity, MapActivity::class.java)
            startActivityForResult(intent,0)
        }// ?????? ?????? ??? MapActivity ??????
        binding.getImageBtn.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }// ?????? ?????? ??? ImageActivity ??????
        binding.submitBtn.setOnClickListener {
            val userSealed: UserSealed = journalViewModel.getUserSealed()!!
            journalViewModel.clearRow(userSealed.userId, journalTimeStamp!!)

            val journal: Journal = if (!journalWriteMode) {
                journalViewModel.journalCache!!.mainJournalContent = binding.diaryBody.text.toString()
                journalViewModel.journalCache!!
            } else {
                // Category
                val journalCategory: JournalCategory = categorySelectionToEnum(
                    binding.categorySpinner.selectedItem.toString()
                )

                // Diary
                val journalWeather: String =
                    binding.weatherSpinner.selectedItem.toString()

                Journal(
                    mainTitle = binding.editText4.text.toString(),
                    mainJournalContent = binding.diaryBody.text.toString(),
                    journalLocation = journalLocation, // TODO: For now, just set to test
                    journalCategory = journalCategory,
                    journalWeather = journalWeather,
                    journalDate = journalTimeStamp!!,
                    journalImage = JournalImage(
                        journalImage?.imageFile
                    )
                )
            }
            journalViewModel.registerJournal(journal,
                _onFailure = {_onFailure(requireContext(), it)}
            )
        }

        binding.tempSave.setOnClickListener {
            tempSave()
        }

    }

    private fun tempSave() {
        val userSealed: UserSealed = journalViewModel.getUserSealed() ?: return
        val tempObject: TempJournal = TempJournal(
            id = null,
            userId = userSealed.userId,
            mainJournalContent = binding.diaryBody.text.toString(),
            journalLocation = journalLocation,
            journalCategory = binding.categorySpinner.selectedItem.toString(),
            journalWeather = binding.weatherSpinner.selectedItem.toString(),
            journalDate = journalTimeStamp!!,
            journalImage = journalImage?.imageFile,
            mainTitle = binding.editText4.text.toString()
        )

        journalViewModel.tempSaveJournal(
            tempJournal = tempObject,
            onSuccess = {Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()},
            onFailure = {Toast.makeText(requireContext(), "Saving failed!", Toast.LENGTH_SHORT).show()}
        )
    }

    private fun attachAdapter() {
        val categorySpinner: Spinner = binding.categorySpinner
        val weatherSpinner: Spinner = binding.weatherSpinner
        categorySpinner.adapter = category
        weatherSpinner.adapter = weather
    }

    private fun categorySelectionToEnum(selection: String): JournalCategory {
        return when (selection) {
            "????????????" -> JournalCategory.THANKS_JOURNAL
            "??????????????????" -> JournalCategory.ACHIEVEMENT_JOURNAL
            "????????????" -> JournalCategory.EMOTIONAL_JOURNAL
            else -> JournalCategory.EMOTIONAL_JOURNAL
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0){
            if(resultCode == Activity.RESULT_OK){
                journalLocation = data!!.action!!
                requireActivity().intent.putExtra("IN", "IN")
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