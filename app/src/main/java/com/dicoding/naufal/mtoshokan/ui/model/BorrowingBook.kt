package com.dicoding.naufal.mtoshokan.ui.model

import android.os.Parcelable
import com.dicoding.naufal.mtoshokan.ui.utils.getNextTenDays
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class BorrowingBook(
    var borrowingId: Long?,
    var book: Book?,
    var borrower: User?,
    var borrowingDate: Date?,
    var returningDate: Date?,
    var isReturned: Boolean?
) : Parcelable

val borrowingBookList = mutableListOf<BorrowingBook>(
    BorrowingBook(1, bookHyouka, userNaufal, Date(), getNextTenDays(), true),
    BorrowingBook(2, bookCreditRoll, userNaufal, Date(), getNextTenDays(), true),
    BorrowingBook(3, bookKudryavka, userNaufal, Date(), getNextTenDays(), false),
    BorrowingBook(4, bookOnajiYume, userNaufal, Date(), getNextTenDays(-1), false)
)