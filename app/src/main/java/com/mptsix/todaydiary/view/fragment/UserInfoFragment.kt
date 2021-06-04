package com.mptsix.todaydiary.view.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.FragmentMainBinding
import com.mptsix.todaydiary.databinding.FragmentUserInfoBinding
import com.mptsix.todaydiary.viewmodel.JournalViewModel
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel

class UserInfoFragment : Fragment() {
    private var _fragmentUserInfoBinding: FragmentUserInfoBinding? = null
    private val fragmentUserInfoBinding: FragmentUserInfoBinding get() = _fragmentUserInfoBinding!!
    //private val ProfileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentUserInfoBinding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return fragmentUserInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUserInfo()
        initPieChart()
        changeUserPwd()
        deleteUser()
    }

    private fun deleteUser(){
        fragmentUserInfoBinding.deleteUser.setOnClickListener {

        }
    }
    private fun changeUserPwd(){
        fragmentUserInfoBinding.changePwd.setOnClickListener {

        }
    }

    private fun initPieChart(){
        fragmentUserInfoBinding.apply {
            pieChart.addPieSlice(
                PieModel("Category1", 1.0f, Color.parseColor("#FFA726"))
            )
            pieChart.addPieSlice(
                PieModel("Category2",1.0f,Color.parseColor("#66BB6A"))
            )
            pieChart.addPieSlice(
                PieModel("Category3",1.0f, Color.parseColor("#EF5350"))
            )
            pieChart.addPieSlice(
                PieModel("Category4", 1.0f, Color.parseColor("#29B6F6"))
            )
        }
    }

    private fun initUserInfo(){
        // 가져온 유저 정보를 각각에 넣음
        fragmentUserInfoBinding.userName.text// =  유저이름
        fragmentUserInfoBinding.userEmail.text// = 유저이메일
    }
}