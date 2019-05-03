package com.dicoding.naufal.mtoshokan.ui.ui.book.borrower.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.model.BorrowingBook
import kotlinx.android.synthetic.main.item_history_borrower.view.*

class BookBorrowerAdapter(private val list: MutableList<BorrowingBook>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == VIEW_TYPE_NORMAL) {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_history_borrower, parent, false)
            )
        } else {
            EmptyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_history_borrower_empty, parent, false))
        }
    }

    override fun getItemCount(): Int {
        return if(list.size == 0){
            1
        } else {
            list.size
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(list.size == 0){
            VIEW_TYPE_EMPTY
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == VIEW_TYPE_NORMAL){
            (holder as ViewHolder).bind(list.get(position))
        }
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun bind(borrowingBook: BorrowingBook){
            view.txt_borrower_name.text = borrowingBook.borrower?.userFullName
            view.txt_date.text = view.context.resources.getString(R.string.range_date, borrowingBook.borrowingDate, borrowingBook.returningDate)
        }
    }

    class EmptyViewHolder(private val view: View) : RecyclerView.ViewHolder(view){

    }

    companion object {
        const val VIEW_TYPE_EMPTY = 0
        const val VIEW_TYPE_NORMAL = 1
    }
}