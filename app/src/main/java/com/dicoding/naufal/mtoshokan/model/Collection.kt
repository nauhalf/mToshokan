package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Collection(
    var collectionId: String?,
    var book: Book?,
    var available: Boolean
) : Parcelable