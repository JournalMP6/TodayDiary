package com.mptsix.todaydiary.view.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.FragmentEditDiaryBinding
import com.mptsix.todaydiary.view.MapActivity

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
        init()
        //getLocation()
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _fragmentEditDiaryBinding = null
    }

    private fun init() {
        lateinit var intent: Intent
        lateinit var journalLocation: String
        var journalCategory =
            fragmentEditDiaryBinding.categorySpinner.selectedItem.toString() // diary category
        var journalWeather =
            fragmentEditDiaryBinding.weatherSpinner.selectedItem.toString() // diary weather
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0){
            if(resultCode == 0){
                Toast.makeText(activity, data!!.toString(),Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(activity, "fail",Toast.LENGTH_SHORT).show()
            }
        }else if(requestCode == 1){
            var photoUri:Uri? = data?.data
            Toast.makeText(activity, photoUri.toString(),Toast.LENGTH_SHORT).show()
        }
    }
}