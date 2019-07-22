package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Suppress("UNCHECKED_CAST")
@Parcelize
data class Bookmark(
    var bookmarkId: String? = null,
    var bookmarkTitle: String? = null,
    var bookmarkDescription: String? = null,
    var bookmarkImages: List<String>? = null,
    var userId: String? = null,
    var bookId: String? = null,
    @ServerTimestamp
    var created: Date? = null
) : Parcelable {
    companion object {
        fun toBookmark(snapshot: DocumentSnapshot?): Bookmark {
            return Bookmark(
                bookmarkId = snapshot?.getString("bookmarkId"),
                bookmarkTitle = snapshot?.getString("bookmarkTitle"),
                bookmarkDescription = snapshot?.getString("bookmarkDescription"),
                bookmarkImages = snapshot?.get("bookmarkImages") as List<String>?,
                userId = snapshot?.getString("userId"),
                bookId = snapshot?.getString("bookId"),
                created = snapshot?.getDate("created")
            )
        }
    }
}