package com.mptsix.todaydiary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView

class JournalRVAdapter : RecyclerView.Adapter<JournalRVAdapter.ViewHolder>() {

    //private val profileViewModel: ProfileViewModel by viewModels()

    inner class ViewHolder(journal: View):RecyclerView.ViewHolder(journal){
        val journalBody : TextView = journal.findViewById(R.id.journalBody)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: JournalRVAdapter.ViewHolder, position: Int) {
        holder.journalBody.text// = 가져온 일기 내용을 삽입
    }

    override fun getItemCount(): Int {
        return 3
    //return profileViewModel.sealedData.size
    }
}