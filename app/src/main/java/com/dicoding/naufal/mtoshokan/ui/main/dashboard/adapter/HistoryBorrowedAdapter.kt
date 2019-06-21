package com.dicoding.naufal.mtoshokan.ui.main.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.BorrowingBook

class HistoryBorrowedAdapter(private val list: MutableList<BorrowingBook>, val listener: (Book) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = if (viewType == VIEW_TYPE_EMPTY) {
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

        return view
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
//            view.txt_title.text = book.book?.bookTitle
//            view.setOnClickListener {
//                book.book?.let { it1 -> listener(it1) }
//            }
        }
    }

    inner class ViewHolderEmpty(private val view: View) : RecyclerView.ViewHolder(view) {

    }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }
}