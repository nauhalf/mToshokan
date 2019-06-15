package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Bookmark(
    var bookmarkId: Long?,
    var bookmarkTitle: String?,
    var bookmarkDescription: String?,
    var bookmarkImage: List<String>
) : Parcelable

val bookmarkHyouka1 = Bookmark(
    1,
    "Kalimat penting",
    "-",
    listOf()
)

val bookmarkHyouka2 = Bookmark(
    2,
    "Jawaban teka teki",
    "-",
    listOf()
)

val bookmarkCreditRoll1 = Bookmark(
    3,
    "Epic moment",
    "-",
    listOf()
)