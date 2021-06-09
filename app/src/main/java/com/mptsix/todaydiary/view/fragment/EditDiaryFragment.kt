package com.mptsix.todaydiary.view.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.data.response.Journal
import com.mptsix.todaydiary.data.response.JournalCategory
import com.mptsix.todaydiary.data.response.JournalImage
import com.mptsix.todaydiary.databinding.FragmentEditDiaryBinding
import com.mptsix.todaydiary.view.activity.MapActivity
import com.mptsix.todaydiary.viewmodel.JournalViewModel
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException

import javax.xml.bind.DatatypeConverter

class EditDiaryFragment : SuperFragment<FragmentEditDiaryBinding>() {
    // Diary Target Date
    private var journalTimeStamp: Long? = null
    private var journalWriteMode: Boolean = false // true for write, false for edit

    private val journalViewModel: JournalViewModel by activityViewModels()

    // Data
    private var journalImage: JournalImage? = null

    // Location
    private var journalLocation: String = ""

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditDiaryBinding {
        journalTimeStamp = arguments?.getLong("timeStamp")
        journalWriteMode = arguments?.getBoolean("modeType") ?: false
        Log.d(this::class.java.simpleName, "Target Time Stamp: $journalTimeStamp")
        Log.d(this::class.java.simpleName, "Target Mode: $journalWriteMode")

        return FragmentEditDiaryBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        attachAdapter()
        init()
        journalViewModel.isJournalSubmit.observe(viewLifecycleOwner) {
            if(it) Toast.makeText(requireContext(), "Submit: $it", Toast.LENGTH_LONG).show()
        }
        applyMode()
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
        }// 버튼 클릭 시 MapActivity 호출
        binding.getImageBtn.setOnClickListener {
            val intent = Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1)
        }// 버튼 클릭 시 ImageActivity 호출
        binding.submitBtn.setOnClickListener {
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
                _onFailure = {
                 when(it){
                     is ConnectException, is SocketTimeoutException -> showDialog("Server Error", "서버 상태가 불안정합니다. \n잠시 후에 다시 시도해주세요.")
                     is RuntimeException -> {
                        showDialog("접속이 끊어졌습니다.", "로그인 페이지로 이동합니다.")
                         // Go back login activity?
                     }
                     else -> {
                         Toast.makeText(context, "알 수 없는 에러가 발생했습니다. 메시지: ${it.message}", Toast.LENGTH_SHORT).show()
                     }
                 }

            })
        }

    }

    private fun attachAdapter() {
        val categorySpinner: Spinner = binding.categorySpinner
        val category = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category,
            R.layout.spinner_layout
        ).apply {
            setDropDownViewResource(R.layout.spinner_layout)
        }
        val weatherSpinner: Spinner = binding.weatherSpinner
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
    private fun showDialog(title:String, message: String){
        val builder: AlertDialog.Builder? = this.let{
            AlertDialog.Builder(requireContext())
        }
        builder?.setMessage(message)
            ?.setTitle(title)
            ?.setPositiveButton("확인"){
                    _, _ ->
            }

        val dialog: AlertDialog?= builder?.create()
        dialog?.show()
    }
}