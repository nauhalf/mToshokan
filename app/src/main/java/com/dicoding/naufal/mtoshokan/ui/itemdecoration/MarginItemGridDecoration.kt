package com.dicoding.naufal.mtoshokan.ui.itemdecoration

import android.R.attr.bottom
import android.R.attr.right
import android.R.attr.left
import android.R.attr.top
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class MarginItemGridDecoration(private val mSizeGridSpacingPx: Float, private val mGridSize: Int) :
    RecyclerView.ItemDecoration() {

    private var mNeedLeftSpacing = false

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val frameWidth = ((parent.width - mSizeGridSpacingPx * (mGridSize - 1)) / mGridSize).toInt()
        val padding = parent.width / mGridSize - frameWidth
        val itemPosition = (view.getLayoutParams() as RecyclerView.LayoutParams).viewAdapterPosition
        if (itemPosition < mGridSize) {
            outRect.top = 0
        } else {
            outRect.top = mSizeGridSpacingPx.toInt()
        }
        if (itemPosition % mGridSize == 0) {
            outRect.left = 0
            outRect.right = padding
            mNeedLeftSpacing = true
        } else if ((itemPosition + 1) % mGridSize == 0) {
            mNeedLeftSpacing = false
            outRect.right = 0
            outRect.left = padding
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false
            outRect.left = mSizeGridSpacingPx.toInt() - padding
            if ((itemPosition + 2) % mGridSize == 0) {
                outRect.right = mSizeGridSpacingPx.toInt() - padding
            } else {
                outRect.right = mSizeGridSpacingPx.toInt() / 2
            }
        } else if ((itemPosition + 2) % mGridSize == 0) {
            mNeedLeftSpacing = false
            outRect.left = mSizeGridSpacingPx.toInt() / 2
            outRect.right = mSizeGridSpacingPx.toInt() - padding
        } else {
            mNeedLeftSpacing = false
            outRect.left = mSizeGridSpacingPx.toInt() / 2
            outRect.right = mSizeGridSpacingPx.toInt() / 2
        }
        outRect.bottom = 0
    }
}