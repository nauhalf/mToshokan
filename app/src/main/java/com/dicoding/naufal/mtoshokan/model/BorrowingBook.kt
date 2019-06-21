package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class BorrowingBook(
    var borrowingBookId: String? = null,
    var collectionId: String? = null,
    var borrower: User? = null,
    var bookId: String? = null,
    var borrowingDate: Date? = null,
    var returningDate: Date? = null,
    var isReturned: Boolean? = null


) : Parcelable

//val borrowingBookList = mutableListOf<BorrowingBook>(
//    BorrowingBook(1, bookHyouka, userNaufal, Date(), getNextTenDays(), true),
//    BorrowingBook(2, bookCreditRoll, userNaufal, Date(), getNextTenDays(), true),
//    BorrowingBook(3, bookKudryavka, userNaufal, Date(), getNextTenDays(), false),
//    BorrowingBook(4, bookOnajiYume, userNaufal, Date(), getNextTenDays(-1), false)
//)