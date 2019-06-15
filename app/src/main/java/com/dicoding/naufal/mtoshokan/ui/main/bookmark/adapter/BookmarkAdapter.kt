package com.dicoding.naufal.mtoshokan.ui.main.bookmark.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.BookmarkBook
import kotlinx.android.synthetic.main.item_bookmarked_book.view.*

class BookmarkAdapter(private val list: MutableList<BookmarkBook>, private val listener: (Book) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_bookmarked_book, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(list.get(position))
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun bind(bookmark: BookmarkBook){
            view.txt_title.text = bookmark.book.bookTitle
            view.txt_bookmark_count.text = bookmark.bookmark?.size.toString()
            view.setOnClickListener {
                listener(bookmark.book)
            }
        }
    }
}