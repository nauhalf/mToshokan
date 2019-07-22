package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import android.util.Patterns
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var userId: String? = null,
    var userFullName: String?= null,
    var userEmail: String?= null,
    var userPhoto: String?= null
) : Parcelable {
    companion object{
        fun toUser(snapshot: DocumentSnapshot?): User{
            return User(
                userId = snapshot?.getString("userId"),
                userFullName = snapshot?.getString("userFullName"),
                userEmail = snapshot?.getString("userEmail"),
                userPhoto = snapshot?.getString("userPhoto")
            )
        }
    }
}

val userNaufal = User(
    "1", "Muhammad Naufal Fadhillah", "naufal@gmail.com", null
)