package com.dicoding.naufal.mtoshokan.itemdecoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemVerticalDecoration(private val spaceHeight: Float) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) != 0) {
                top = spaceHeight.toInt()
            }
        }
    }
}