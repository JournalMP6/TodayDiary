package com.mptsix.todaydiary.view.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mptsix.todaydiary.data.response.UserFiltered
import com.mptsix.todaydiary.databinding.UserRowBinding
import com.mptsix.todaydiary.view.activity.FollowerActivity
import com.mptsix.todaydiary.view.fragment.UserSearchFragment
import kotlinx.coroutines.withContext

class UserRVAdapter(var link : UserSearchFragment.getUserId): RecyclerView.Adapter<UserRVAdapter.ViewHolder>() {
    interface OnItemClickListener{
        fun OnItemClick(holder: ViewHolder, view: View, position: Int)
    }
    var itemClickListener: OnItemClickListener?=null
    var userList: List<UserFiltered> = listOf()
        set(value){
            Log.d(this::class.java.simpleName, "Setting User")
            value.forEach {
                Log.d(this::class.java.simpleName, "id: "+it.userId+" name: "+it.userName)
            }
            field = value
            notifyDataSetChanged()
        }
    inner class ViewHolder(private val rowBinding: UserRowBinding):RecyclerView.ViewHolder(rowBinding.root){
        fun initView(userFiltered: UserFiltered) {
            if (userFiltered.isUserFollowedTargetUser == true){
                rowBinding.followBtn.text = "following"
            }
            Log.d(this::class.java.simpleName, "Inititating view!")
            rowBinding.userName.text = userFiltered.userName
            rowBinding.userEmail.text = userFiltered.userId
        }
        fun followUser(){
            rowBinding.followBtn.setOnClickListener {
                if(rowBinding.followBtn.text == "follow"){
                    rowBinding.followBtn.text = "following"
                    link.getFollowUserId(rowBinding.userEmail.text.toString())
                }else{
                    rowBinding.followBtn.text = "follow"
                    link.getUnfollowUserId(rowBinding.userEmail.text.toString())
                }
            }
        }
        fun showFollowerPage(userFiltered: UserFiltered){
            rowBinding.root.setOnClickListener {
                itemClickListener?.OnItemClick(this,it,adapterPosition)
                link.setFollowerProfile(userFiltered.userId)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(userList[position])
        holder.followUser()
        holder.showFollowerPage(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}