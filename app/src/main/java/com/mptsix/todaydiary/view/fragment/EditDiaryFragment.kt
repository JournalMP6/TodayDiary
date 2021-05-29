package com.mptsix.todaydiary.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.FragmentEditDiaryBinding

class EditDiaryFragment : Fragment() {
    private var _fragmentEditDiaryBinding: FragmentEditDiaryBinding? = null
    private val fragmentEditDiaryBinding: FragmentEditDiaryBinding get() = _fragmentEditDiaryBinding!!

    // Diary Target Date
    private var journalTimeStamp: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        journalTimeStamp = arguments?.getLong("timeStamp")
        Log.d(this::class.java.simpleName, "Target Time Stamp: $journalTimeStamp")
        _fragmentEditDiaryBinding = FragmentEditDiaryBinding.inflate(inflater, container, false)
        return fragmentEditDiaryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        attachAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _fragmentEditDiaryBinding = null
    }

    private fun attachAdapter(){
        val spinner: Spinner = fragmentEditDiaryBinding.categorySpinner
        val category = ArrayAdapter.createFromResource(requireContext(),
            R.array.category,
            R.layout.spinner_layout
        ).apply {
            setDropDownViewResource(R.layout.spinner_layout)
        }
        spinner.adapter = category
    }
}