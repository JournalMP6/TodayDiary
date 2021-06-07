package com.mptsix.todaydiary.view.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mptsix.todaydiary.view.adapter.JournalRVAdapter
import com.mptsix.todaydiary.data.response.JournalCategoryResponse
import com.mptsix.todaydiary.databinding.FragmentUserInfoBinding
import com.mptsix.todaydiary.view.PwdChangeActivity
import com.mptsix.todaydiary.viewmodel.ProfileViewModel
import org.eazegraph.lib.models.PieModel

class UserInfoFragment : Fragment() {
    private var _fragmentUserInfoBinding: FragmentUserInfoBinding? = null
    private val fragmentUserInfoBinding: FragmentUserInfoBinding get() = _fragmentUserInfoBinding!!
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private val journalRVAdapter: JournalRVAdapter = JournalRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentUserInfoBinding = FragmentUserInfoBinding.inflate(inflater, container, false)
        return fragmentUserInfoBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeUserPwd()
        deleteUser()
        followUser()

        // RecyclerView
        fragmentUserInfoBinding.journalRecycler.adapter = journalRVAdapter
        fragmentUserInfoBinding.journalRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        profileViewModel.userRemoveSucceed.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Removing user succeed!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Error occurred when removing user.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        profileViewModel.sealedData.observe(viewLifecycleOwner) {
            if (it != null) {
                // 가져온 유저 정보를 각각에 넣음
                fragmentUserInfoBinding.userName.text = it.userName
                fragmentUserInfoBinding.userEmail.text = it.userId
                initPieChart(it.journalCategoryList)
                journalRVAdapter.journalList = it.journalList
            }
        }

        profileViewModel.getSealedData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentUserInfoBinding = null
    }

    private fun deleteUser(){
        fragmentUserInfoBinding.deleteUser.setOnClickListener {
            profileViewModel.removeUser()
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
        profileViewModel.getFollowingUser()

        profileViewModel.followingUserList.observe(viewLifecycleOwner){
            fragmentUserInfoBinding.followNum.text = "Follower: " + it.size.toString()
        }
    }// follower 수를 보여줌

    private fun initPieChart(journalCategoryList: List<JournalCategoryResponse>){
        fragmentUserInfoBinding.apply {
            val colorList: List<Int> = listOf(
                Color.parseColor("#FFA726"),
                Color.parseColor("#66BB6A"),
                Color.parseColor("#EF5350"),
                Color.parseColor("#29B6F6")
            )

            for (i in journalCategoryList.indices) {
                Log.d(this::class.java.simpleName, "Category: ${journalCategoryList[i].category.name}, Count: ${journalCategoryList[i].count}")
                pieChart.addPieSlice(
                    PieModel(journalCategoryList[i].category.name, journalCategoryList[i].count.toFloat(), colorList[i])
                )
            }
        }
    }
}