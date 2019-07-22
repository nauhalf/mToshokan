package com.dicoding.naufal.mtoshokan.ui.main.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.BorrowingBook
import kotlinx.android.synthetic.main.item_borrowed_books_horizontal.view.*

class HistoryBorrowedAdapter(private val list: MutableList<BorrowingBook> = mutableListOf(), val listener: (BorrowingBook) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(data: List<BorrowingBook>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == VIEW_TYPE_EMPTY) {
            ViewHolderEmpty(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_borrowed_books_horizontal_empty, parent, false)
            )
        } else {
            ViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_borrowed_books_horizontal, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return if (list.size == 0) {
            1
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (list.size == 0) {
            VIEW_TYPE_EMPTY
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_EMPTY -> {

            }
            VIEW_TYPE_NORMAL -> {
                (holder as ViewHolder).bind(list.get(position))
            }
        }
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(book: BorrowingBook) {
            view.txt_title.text = book.book?.bookTitle
            view.setOnClickListener {
                 listener(book)
            }
            Glide.with(view.context)
                .load(book.book?.bookCover)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(view.img_cover)
        }
    }

    inner class ViewHolderEmpty(private val view: View) : RecyclerView.ViewHolder(view) {

    }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }
}