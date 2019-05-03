package com.dicoding.naufal.mtoshokan.ui.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.model.SearchHistory
import kotlinx.android.synthetic.main.item_search_history.view.*

class SearchHistoryAdapter(private val list: MutableList<SearchHistory>, val listener: (SearchHistory)-> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_search_history, parent, false)
        )
    }

    fun delete(s: SearchHistory) : Unit{
        list.remove(s)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindItem(list.get(position))
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(s: SearchHistory) {
            val chip = view.chip_history
            chip.text = s.text
            chip.setOnCloseIconClickListener {
                delete(s)
            }

            chip.setOnClickListener {
                listener(s)
            }
        }
    }
}