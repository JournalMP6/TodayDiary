package com.mptsix.todaydiary.view.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.databinding.FragmentUserInfoBinding
import com.mptsix.todaydiary.view.PwdChangeActivity
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
        followUser()
    }

    private fun deleteUser(){
        fragmentUserInfoBinding.deleteUser.setOnClickListener {

        }
    }// 버튼 클릭 시, 서버에서 유저 삭제

    private fun changeUserPwd(){
        lateinit var intent: Intent
        fragmentUserInfoBinding.changePwd.setOnClickListener {
            intent = Intent(activity, PwdChangeActivity::class.java)
            startActivity(intent)
        }
    }//비밀번호 변경 클릭 시, 비밀번호 변경 activity로 화면 전환

    private fun followUser(){
        fragmentUserInfoBinding.apply {
            followBtn.setOnClickListener {
                if(followBtn.text == "Follow"){
                    followBtn.text = "Followed"
                }else{
                    followBtn.text = "Follow"
                }
            }
        }
    }// 팔로우 버튼을 누르면 follow일 시 followed로 변경, followed일 시 follow로 변경 *****누른 유저와 눌리는 유저의 정보가 있어야함*****

    private fun initPieChart(){
        fragmentUserInfoBinding.apply {
            pieChart.addPieSlice(//_value 값에서 %를 계산해 넣어야함
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