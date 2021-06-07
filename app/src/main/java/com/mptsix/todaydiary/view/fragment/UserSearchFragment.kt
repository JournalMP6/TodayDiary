package com.mptsix.todaydiary.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mptsix.todaydiary.view.adapter.UserRVAdapter
import com.mptsix.todaydiary.databinding.FragmentUserSearchBinding
import com.mptsix.todaydiary.viewmodel.UserListViewModel

class UserSearchFragment : Fragment() {

    private var _fragmentUserSearchBinding: FragmentUserSearchBinding? = null
    private val fragmentUserSearchBinding: FragmentUserSearchBinding get() = _fragmentUserSearchBinding!!
    private val userListViewModel : UserListViewModel by activityViewModels()
    private var userRVAdapter: UserRVAdapter?= null

    inner class getUserId{
        fun getFollowUserId(userId : String){
            userListViewModel.followUser(userId)
        }

        fun getUnfollowUserId(userId : String){
            userListViewModel.unfollowUser(userId)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragmentUserSearchBinding = FragmentUserSearchBinding.inflate(inflater, container, false)
        return fragmentUserSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userRVAdapter =  UserRVAdapter(getUserId())
        fragmentUserSearchBinding.userRecyclerView.adapter = userRVAdapter

        userListViewModel.userFilteredList.observe(viewLifecycleOwner){
            userRVAdapter?.userList = it
        }


        fragmentUserSearchBinding.searchBtn.setOnClickListener {
            var userName = fragmentUserSearchBinding.searchUserName.text.toString()
            userListViewModel.findUserByUserName(userName)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //뷰모델에서 데이터 삭제해야할 것 같음. 화면 새로 불러와도 데이터가 초기화 안됨
        _fragmentUserSearchBinding = null
    }
}