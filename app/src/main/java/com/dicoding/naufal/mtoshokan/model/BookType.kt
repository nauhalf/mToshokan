package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookType(
    var typeId: String?= null,
    var typeName: String?= null
) : Parcelable

val bookTypeNovel = BookType("qq", "Novel")