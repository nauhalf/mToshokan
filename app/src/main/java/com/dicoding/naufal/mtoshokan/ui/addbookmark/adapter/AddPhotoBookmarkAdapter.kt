package com.dicoding.naufal.mtoshokan.ui.addbookmark.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R

class AddPhotoBookmarkAdapter(private val list: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if(viewType == VIEW_TYPE_FOOTER){
            AddPhotoViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_add_photo, parent, false)
            )
        } else {
            ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_add_photo_bookmark, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return if(getItemViewType(position) == VIEW_TYPE_FOOTER){
            (holder as AddPhotoViewHolder).bind()
        } else {
            (holder as ViewHolder).bind(list.get(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(list.size == position) VIEW_TYPE_FOOTER else VIEW_TYPE_NORMAL
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun bind(photo: String){

        }
    }

    inner class AddPhotoViewHolder(private val view:View) : RecyclerView.ViewHolder(view){
        fun bind(){
            view.setOnClickListener {
                Toast.makeText(view.context, "Add Photo", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val VIEW_TYPE_FOOTER = 0
        const val VIEW_TYPE_NORMAL = 1
    }
}