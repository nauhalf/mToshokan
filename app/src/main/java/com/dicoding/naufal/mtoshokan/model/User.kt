package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import android.util.Patterns
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var userId: String?,
    var userFullName: String?,
    var userEmail: String?,
    var userPhoto: String?
) : Parcelable

val userNaufal = User(
    "1", "Muhammad Naufal Fadhillah", "naufal@gmail.com", null
)