package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTime
import java.util.*

@Parcelize
data class SearchHistory(
    var text: String?,
    var time: Date?
) : Parcelable


var searchHistory = mutableListOf<SearchHistory>(
    SearchHistory("Hyouka", DateTime.parse("2019-01-29T10:11:23").toDate()),

    SearchHistory("Buku Bagus", DateTime.parse("2019-01-29T10:12:24").toDate()),

    SearchHistory("Same Dream", DateTime.parse("2019-01-29T10:15:25").toDate())
)