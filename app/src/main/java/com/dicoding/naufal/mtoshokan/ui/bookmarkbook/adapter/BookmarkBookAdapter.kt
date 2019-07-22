package com.dicoding.naufal.mtoshokan.ui.bookmarkbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.model.Bookmark
import kotlinx.android.synthetic.main.item_bookmark_list.view.*

class BookmarkBookAdapter(private val list: MutableList<Bookmark> = mutableListOf(), private val listener : (Bookmark) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(data: List<Bookmark>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_NORMAL) {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark_list, parent, false)
            )
        } else {
            EmptyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bookmark_empty, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if (list.size == 0) {
            1
        } else {
            list.size
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            (holder as ViewHolder).bind(list.get(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.size == 0) {
            VIEW_TYPE_EMPTY
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(bookmark: Bookmark) {
            view.txt_bookmark_title.text = bookmark.bookmarkTitle
            view.txt_bookmark_description.text = bookmark.bookmarkDescription
            view.txt_img_count.text = bookmark.bookmarkImages?.size.toString()
            view.setOnClickListener {
                listener(bookmark)
            }
        }
    }

    class EmptyViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }
}