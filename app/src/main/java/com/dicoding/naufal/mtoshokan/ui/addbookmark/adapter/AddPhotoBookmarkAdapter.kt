package com.dicoding.naufal.mtoshokan.ui.addbookmark.adapter

import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.R
import kotlinx.android.synthetic.main.item_add_photo_bookmark.view.*

class AddPhotoBookmarkAdapter(private val list: MutableList<Uri> = mutableListOf(), private val addListener: () -> Unit, private val photoListener: (Uri) -> Unit, private val deleteListener: (Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(data: List<Uri>){
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

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
            (holder as ViewHolder).bind(list.get(position-1), position-1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0) VIEW_TYPE_FOOTER else VIEW_TYPE_NORMAL
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        fun bind(uri: Uri, i: Int){
            Glide.with(view.context)
                .load(uri)
                .placeholder(ColorDrawable(ContextCompat.getColor(view.context, R.color.colorInactive)))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(view.img_add_photo)
            view.setOnClickListener {
                photoListener(uri)
            }

            view.txt_delete.setOnClickListener {
                deleteListener(i)
            }
        }
    }

    inner class AddPhotoViewHolder(private val view:View) : RecyclerView.ViewHolder(view){
        fun bind(){
            view.setOnClickListener {
                if(list.size <5 ){
                    addListener()
                } else {
                    Toast.makeText(view.context, "1 penanda hanya boleh 5 gambar.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_FOOTER = 0
        const val VIEW_TYPE_NORMAL = 1
    }
}