package com.mptsix.todaydiary.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.mptsix.todaydiary.view.adapter.UserRVAdapter
import com.mptsix.todaydiary.databinding.FragmentUserSearchBinding
import com.mptsix.todaydiary.view.activity.FollowerActivity
import com.mptsix.todaydiary.viewmodel.ProfileViewModel
import com.mptsix.todaydiary.viewmodel.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.RuntimeException
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@AndroidEntryPoint
class UserSearchFragment @Inject constructor() : SuperFragment<FragmentUserSearchBinding>() {
    private val userListViewModel : UserListViewModel by viewModels()
    private val profileViewModel : ProfileViewModel by viewModels()
    private var userRVAdapter: UserRVAdapter?= null

    inner class getUserId{
        fun getFollowUserId(userId : String){
            userListViewModel.followUser(userId,
            _onFailure = {
                _onFailure(requireContext(),it)
            })
        }

        fun getUnfollowUserId(userId : String){
            userListViewModel.unfollowUser(userId,
            _onFailure = {
                _onFailure(requireContext(), it)
            })
        }
        fun setFollowerProfile(userId: String){
            profileViewModel.getSealedUserByUserId(userId,
            _onFailure = {
                _onFailure(requireContext(), it)
            })
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
        userRVAdapter?.itemClickListener = object:UserRVAdapter.OnItemClickListener{
            override fun OnItemClick(holder: UserRVAdapter.ViewHolder, view: View, position: Int) {
                val followerPageIntent = Intent(context, FollowerActivity::class.java)
                followerPageIntent.putExtra("userId",userRVAdapter!!.userList[position].userId)
                followerPageIntent.putExtra("isFollowing",userRVAdapter!!.userList[position].isUserFollowedTargetUser)
                startActivityForResult(followerPageIntent, 100)
            }
        }

        userListViewModel.userFilteredList.observe(viewLifecycleOwner){
            userRVAdapter?.userList = it
        }


        binding.searchBtn.setOnClickListener {
            var userName = binding.searchUserName.text.toString()
            userListViewModel.findUserByUserName(userName,
             _onFailure = {
                    when(it){
                        is ConnectException, is SocketTimeoutException -> showDialog(requireContext(),"Server Error", "서버 상태가 불안정합니다. \n잠시 후에 다시 시도해주세요.")
                        is RuntimeException -> {
                            showDialog(requireContext(),"접속이 끊어졌습니다.", "로그인 페이지로 이동합니다.")
                            // Go back login activity?
                        }
                        is NoSuchFieldException -> Toast.makeText(context, "검색어를 입력해주시기 바랍니다.", Toast.LENGTH_SHORT).show()
                        else -> Toast.makeText(context, "알 수 없는 에러가 발생했습니다. 메시지: ${it.message}", Toast.LENGTH_SHORT).show()

                    }
                })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100){
            if(resultCode == Activity.RESULT_OK){
                requireActivity().intent.putExtra("IN", "IN")
            }
        }
    }

}