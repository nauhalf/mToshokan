package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookmarkBook(
    var bookmarkBookId: String? = null
) : Parcelable {
    companion object{
        fun toBookmarkBook(bookmarkBookId: String?, user: User?, book: Book?): BookmarkBook {
            return BookmarkBook(
                bookmarkBookId = bookmarkBookId
            )
        }
    }
}