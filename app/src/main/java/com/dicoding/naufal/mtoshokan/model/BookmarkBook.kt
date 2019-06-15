package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookmarkBook(
    var user: User?,
    var book: Book,
    var bookmark: List<Bookmark>?
) : Parcelable

val bookmarkBook1 = BookmarkBook(
    userNaufal,
    bookHyouka,
    listOf(bookmarkHyouka1, bookmarkHyouka2)
)

val bookmarkBook2= BookmarkBook(
    userNaufal,
    bookCreditRoll,
    listOf(bookmarkCreditRoll1)
)

val bookmarkList = mutableListOf<BookmarkBook>(bookmarkBook1, bookmarkBook2)