package com.dicoding.naufal.mtoshokan.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var userId: Long?,
    var userFullName: String?,
    var userEmail: String?,
    var userPhoto: String?
) : Parcelable

val userNaufal = User(
    1, "Muhammad Naufal Fadhillah", "naufal@gmail.com", null
)