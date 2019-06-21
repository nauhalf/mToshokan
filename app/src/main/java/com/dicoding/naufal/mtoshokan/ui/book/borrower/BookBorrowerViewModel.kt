package com.dicoding.naufal.mtoshokan.ui.book.borrower

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.BorrowingBook
import com.dicoding.naufal.mtoshokan.model.User
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BookBorrowerViewModel : BaseViewModel() {
    val bookId: MutableLiveData<String> = MutableLiveData()
    val borrowerLiveData: MutableLiveData<MutableList<BorrowingBook>> = MutableLiveData()

    fun loadData() {
        try{
            scope.launch {
                loading.value = true
                val result = mutableListOf<BorrowingBook>()
                    withContext(Dispatchers.IO) {
                    val query = database.collection(ConstantValue.Database.BorrowingBooks).whereEqualTo("bookId", bookId.value)
                        .orderBy("borrowingDate", Query.Direction.DESCENDING)
                    val snapshot = query.get().await()

                    snapshot.forEach {
                        val borrower = it.getDocumentReference("user")?.get()?.await()
                        val book = BorrowingBook(
                            borrowingBookId = it.getString("borrowingBookId"),
                            collectionId = it.getString("collectionId"),
                            borrower = borrower?.toObject(User::class.java),
                            bookId = it.getString("bookId"),
                            borrowingDate = it.getDate("borrowingDate"),
                            returningDate = it.getDate("returningDate"),
                            isReturned = it.getBoolean("isReturned")
                        )
                        result.add(book)
                    }
//                    snapshot.map {
//                        val borrower = it.getDocumentReference("user")?.get()?.await()
//                        val book = BorrowingBook(
//                            borrowingBookId = it.getString("borrowingBookId"),
//                            collectionId = it.getString("collectionId"),
//                            borrower = borrower?.toObject(User::class.java),
//                            bookId = it.getString("bookId"),
//                            borrowingDate = it.getDate("borrowingDate"),
//                            returningDate = it.getDate("returningDate"),
//                            isReturned = it.getBoolean("isReturned")
//                        )
//                        it.toObject(BorrowingBook::class.java)
//                    }.toMutableList()
                }
                borrowerLiveData.value = result
                loading.value = false
            }
        }
        catch (e: Exception){
            Log.d("Error => ", e.localizedMessage)
        }

    }
}