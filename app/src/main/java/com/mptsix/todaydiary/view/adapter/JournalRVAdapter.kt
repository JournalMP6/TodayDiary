package com.mptsix.todaydiary.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mptsix.todaydiary.data.internal.JournalSealed
import com.mptsix.todaydiary.databinding.DiaryRowBinding

class JournalRVAdapter : RecyclerView.Adapter<JournalRVAdapter.ViewHolder>() {
    var journalList: List<JournalSealed> = listOf()
        set(value) {
            Log.d(this::class.java.simpleName, "Setting Journal Sealed")
            value.forEach {
                Log.d(this::class.java.simpleName, it.mainContent)
            }
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val rowBinding: DiaryRowBinding):RecyclerView.ViewHolder(rowBinding.root){
        fun initView(journalSealed: JournalSealed) {
            Log.d(this::class.java.simpleName, "Inititating view!")
            rowBinding.journalBody.text = journalSealed.mainContent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DiaryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initView(journalList[position])
    }

    override fun getItemCount(): Int  = journalList.size
}