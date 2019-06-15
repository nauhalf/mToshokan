package com.dicoding.naufal.mtoshokan.ui.searchresult.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.model.Book
import kotlinx.android.synthetic.main.item_search_book.view.*

class SearchResultAdapter(private val list: MutableList<Book>, val listener: (Book)-> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_book, parent, false)
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bindItem(list.get(position))
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bindItem(book: Book){
            view.txt_title.text = book.bookTitle
            view.txt_isbn.text = book.bookISBN
            view.txt_writer.text = book.bookWriter
            view.txt_type.text = book.bookType?.typeName

            view.setOnClickListener {
                listener(book)
            }
        }
    }

}