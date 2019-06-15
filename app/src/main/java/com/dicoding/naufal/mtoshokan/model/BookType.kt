package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookType(
    var typeId: String?,
    var typeName: String?
) : Parcelable

val bookTypeNovel = BookType("qq", "Novel")