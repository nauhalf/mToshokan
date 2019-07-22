package com.dicoding.naufal.mtoshokan.model

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
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
    var isReturned: Boolean? = null,
    var pinaltyAmount: String? = null,
    var book: Book? = null

) : Parcelable{
    companion object{
        fun toBorrowingBook(snapshot: DocumentSnapshot?, borrower: User?, book: Book?): BorrowingBook{
            return BorrowingBook(
                borrowingBookId = snapshot?.getString("borrowingBookId"),
                collectionId = snapshot?.getString("collectionId"),
                borrower = borrower,
                bookId = snapshot?.getString("bookId"),
                borrowingDate = snapshot?.getDate("borrowingDate"),
                returningDate = snapshot?.getDate("returningDate"),
                isReturned = snapshot?.getBoolean("isReturned"),
                pinaltyAmount = snapshot?.getString("pinaltyAmount"),
                book = book
            )
        }
    }
}

//val borrowingBookList = mutableListOf<BorrowingBook>(
//    BorrowingBook(1, bookHyouka, userNaufal, Date(), getNextTenDays(), true),
//    BorrowingBook(2, bookCreditRoll, userNaufal, Date(), getNextTenDays(), true),
//    BorrowingBook(3, bookKudryavka, userNaufal, Date(), getNextTenDays(), false),
//    BorrowingBook(4, bookOnajiYume, userNaufal, Date(), getNextTenDays(-1), false)
//)