package com.mptsix.todaydiary

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.mptsix.todaydiary.data.internal.JournalSealed
import com.mptsix.todaydiary.databinding.RowBinding

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

    inner class ViewHolder(private val rowBinding: RowBinding):RecyclerView.ViewHolder(rowBinding.root){
        fun initView(journalSealed: JournalSealed) {
            Log.d(this::class.java.simpleName, "Inititating view!")
            rowBinding.journalBody.text = journalSealed.mainContent
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalRVAdapter.ViewHolder {
        return ViewHolder(
            RowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: JournalRVAdapter.ViewHolder, position: Int) {
        holder.initView(journalList[position])
    }

    override fun getItemCount(): Int  = journalList.size
}