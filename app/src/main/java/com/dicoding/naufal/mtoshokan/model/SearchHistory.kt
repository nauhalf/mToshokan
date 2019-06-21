package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@IgnoreExtraProperties
@Parcelize
data class SearchHistory(
    var id: String? = null,
    var text: String? = null,
    @ServerTimestamp
    var time: Date? = null,
    var userId: String? = null
) : Parcelable {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "time" to time,
            "text" to text
        )
    }
}


//var searchHistory = mutableListOf(
//    SearchHistory("Hyouka", DateTime.parse("2019-01-29T10:11:23").toDate(), "1"),
//
//    SearchHistory("Buku Bagus", DateTime.parse("2019-01-29T10:12:24").toDate(), "2"),
//
//    SearchHistory("Same Dream", DateTime.parse("2019-01-29T10:15:25").toDate(), "3")
//)