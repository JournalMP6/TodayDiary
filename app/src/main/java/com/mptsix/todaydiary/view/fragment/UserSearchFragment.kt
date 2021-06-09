package com.mptsix.todaydiary.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mptsix.todaydiary.view.adapter.UserRVAdapter
import com.mptsix.todaydiary.databinding.FragmentUserSearchBinding
import com.mptsix.todaydiary.viewmodel.UserListViewModel

class UserSearchFragment : SuperFragment<FragmentUserSearchBinding>() {
    private val userListViewModel : UserListViewModel by viewModels()
    private var userRVAdapter: UserRVAdapter?= null

    inner class getUserId{
        fun getFollowUserId(userId : String){
            userListViewModel.followUser(userId)
        }

        fun getUnfollowUserId(userId : String){
            userListViewModel.unfollowUser(userId)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUserSearchBinding {
        return FragmentUserSearchBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        userRVAdapter =  UserRVAdapter(getUserId())
        binding.userRecyclerView.adapter = userRVAdapter

        userListViewModel.userFilteredList.observe(viewLifecycleOwner){
            userRVAdapter?.userList = it
        }


        binding.searchBtn.setOnClickListener {
            var userName = binding.searchUserName.text.toString()
            userListViewModel.findUserByUserName(userName)
        }
    }
}