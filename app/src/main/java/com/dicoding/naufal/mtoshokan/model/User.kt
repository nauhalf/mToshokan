package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import android.util.Patterns
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var userId: String? = null,
    var userFullName: String?= null,
    var userEmail: String?= null,
    var userPhoto: String?= null
) : Parcelable

val userNaufal = User(
    "1", "Muhammad Naufal Fadhillah", "naufal@gmail.com", null
)