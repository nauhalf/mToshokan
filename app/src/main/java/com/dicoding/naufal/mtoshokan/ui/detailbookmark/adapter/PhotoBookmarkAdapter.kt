package com.dicoding.naufal.mtoshokan.ui.detailbookmark.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.R
import kotlinx.android.synthetic.main.item_add_photo_bookmark.view.*


class PhotoBookmarkAdapter(
    private val list: MutableList<String> = mutableListOf(),
    private val photoListener: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(data: List<String>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_photo_bookmark, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            (holder as ViewHolder).bind(list.get(position), position)

    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(s: String, i: Int) {
            Glide.with(view.context)
                .load(s)
                .placeholder(ColorDrawable(ContextCompat.getColor(view.context, R.color.colorInactive)))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(view.img_add_photo)
            view.setOnClickListener {
                photoListener(s)
            }
        }
    }
}