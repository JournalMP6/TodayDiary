package com.mptsix.todaydiary.view.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.mptsix.todaydiary.view.adapter.JournalRVAdapter
import com.mptsix.todaydiary.data.response.JournalCategoryResponse
import com.mptsix.todaydiary.databinding.FragmentUserInfoBinding
import com.mptsix.todaydiary.view.activity.LoginActivity
import com.mptsix.todaydiary.view.activity.PrimaryLockActivity
import com.mptsix.todaydiary.view.activity.PwdChangeActivity
import com.mptsix.todaydiary.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.eazegraph.lib.models.PieModel
import javax.inject.Inject

@AndroidEntryPoint
class UserInfoFragment @Inject constructor() : SuperFragment<FragmentUserInfoBinding>() {
    private val profileViewModel: ProfileViewModel by activityViewModels()

    private val journalRVAdapter: JournalRVAdapter = JournalRVAdapter()

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserInfoBinding {
        return FragmentUserInfoBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        changeUserPwd()
        deleteUser()
        followUser()
        addAuxiliaryPwd()

        // RecyclerView
        binding.journalRecycler.adapter = journalRVAdapter
        binding.journalRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        profileViewModel.userRemoveSucceed.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(requireContext(), "Removing user succeed!", Toast.LENGTH_SHORT)
                    .show()
                activity?.finishAffinity()
                var intent = Intent(activity, LoginActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            }
        }

        profileViewModel.sealedData.observe(viewLifecycleOwner) {
            if (it != null) {
                // 가져온 유저 정보를 각각에 넣음
                binding.userName.text = it.userName
                binding.userEmail.text = it.userId
                initPieChart(it.journalCategoryList)
                journalRVAdapter.journalList = it.journalList
            }
        }

        profileViewModel.getSealedData(
            _onFailure = {
                _onFailure(requireContext(), it)
            }) // error handling
    }

    private fun deleteUser(){
        binding.deleteUser.setOnClickListener {
            profileViewModel.removeUser(_onFailure = {
                _onFailure(requireContext(), it)
            })
        }
    }// 버튼 클릭 시, 서버에서 유저 삭제

    private fun addAuxiliaryPwd(){
        lateinit var intent:Intent
        binding.addAuxiliaryPwd.setOnClickListener {
            intent = Intent(activity, PrimaryLockActivity::class.java)
            startActivityForResult(intent, 100)
        }
    }

    private fun changeUserPwd(){
        lateinit var intent: Intent
        binding.changePwd.setOnClickListener {
            intent = Intent(activity, PwdChangeActivity::class.java)
            startActivity(intent)
        }
    }//비밀번호 변경 클릭 시, 비밀번호 변경 activity로 화면 전환

    private fun followUser(){
        profileViewModel.getFollowingUser(_onFailure = {
            _onFailure(requireContext(), it)
        })

        profileViewModel.followingUserList.observe(viewLifecycleOwner){
            binding.followNum.text = "Follower: " + it.size.toString()
        }
    }// follower 수를 보여줌

    private fun initPieChart(journalCategoryList: List<JournalCategoryResponse>){
        binding.apply {
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