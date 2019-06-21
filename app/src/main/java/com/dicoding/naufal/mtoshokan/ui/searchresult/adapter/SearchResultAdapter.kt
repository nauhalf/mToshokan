package com.dicoding.naufal.mtoshokan.ui.searchresult.adapter

import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.model.Book
import kotlinx.android.synthetic.main.item_search_book.view.*
import kotlinx.android.synthetic.main.item_search_result_header.view.*

class SearchResultAdapter(val listener: (Book)-> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list: MutableList<Book> = mutableListOf()
    private lateinit var searchQuery: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == TYPE_HEADER){
            HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_search_result_header, parent, false)
            )
        } else {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_search_book, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = list.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is HeaderViewHolder){
            holder.bindItem(searchQuery)
        } else {
            (holder as ViewHolder).bindItem(list[getListPosition(position)])
        }
    }

    private fun getListPosition(position: Int) = position-1

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            TYPE_HEADER
        } else {
            TYPE_ITEM
        }
    }

    fun setData(data: List<Book>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    fun setQuery(s : String){
        searchQuery = s
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bindItem(book: Book){
            view.txt_title.text = book.bookTitle
            view.txt_isbn.text = book.bookISBN
            view.txt_writer.text = book.bookWriter
            view.txt_type.text = book.bookType?.typeName

            Glide.with(view.context)
                .load(book.bookCover)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(view.img_cover)


            view.setOnClickListener {
                listener(book)
            }

        }
    }

    inner class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        fun bindItem(s: String){

            val query = SpannableStringBuilder()
                .append(view.resources.getString(R.string.search_with_keywords))
                .append(" ")
                .bold {
                    append(s)
                }


            view.txt_search_query.text = query
        }
    }

    interface OnBookClickListener {
        fun onClick(book: Book)
    }

    companion object{
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
    }
}