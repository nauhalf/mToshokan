package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Collection(
    var collectionId: String? = null,
    var book: Book? = null,
    var available: Boolean? = null
) : Parcelable {
    companion object {
        fun toCollection(snapshot : DocumentSnapshot, book: Book): Collection {
            return Collection(
                collectionId = snapshot.getString("collectionId"),
                book = book,
                available = snapshot.getBoolean("available")
            )
        }
    }
}