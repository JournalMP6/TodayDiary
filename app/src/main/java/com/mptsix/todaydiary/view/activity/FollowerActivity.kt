package com.mptsix.todaydiary.view.activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.mptsix.todaydiary.R
import com.mptsix.todaydiary.data.response.JournalCategoryResponse
import com.mptsix.todaydiary.databinding.ActivityFollowerBinding
import com.mptsix.todaydiary.view.adapter.JournalRVAdapter
import com.mptsix.todaydiary.viewmodel.ProfileViewModel
import org.eazegraph.lib.models.PieModel

class FollowerActivity : AppCompatActivity() {
    lateinit var binding : ActivityFollowerBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val journalRVAdapter: JournalRVAdapter = JournalRVAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFollowerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initActivity()
    }

    private fun initActivity() {
        binding.FollowerjournalRecycler.adapter = journalRVAdapter
        binding.FollowerjournalRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        followUser()

        profileViewModel.sealedData2.observe(this) {
            if (it != null) {
                // 가져온 유저 정보를 각각에 넣음
                binding.FollowerName.text = it.userName
                binding.FollowerEmail.text = it.userId
                initPieChart(it.journalCategoryList)
                journalRVAdapter.journalList = it.journalList
            }
        }
        val userId = intent.getStringExtra("userId")
        profileViewModel.getSealedUserByUserId(userId!!)
    }

    private fun followUser(){
        profileViewModel.getFollowingUser()

        profileViewModel.followingUserList.observe(this){
            binding.FollowerfollowNum.text = "Follower: " + it.size.toString()
        }
    }

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
                FollowerPieChart.addPieSlice(
                    PieModel(journalCategoryList[i].category.name, journalCategoryList[i].count.toFloat(), colorList[i])
                )
            }
        }
    }
}