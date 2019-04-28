package com.dicoding.naufal.mtoshokan.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookType(
    var typeId: Long?,
    var typeName: String?
) : Parcelable

val bookTypeNovel = BookType(1, "Novel")