package com.dicoding.naufal.mtoshokan.ui.main.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.BookType
import com.dicoding.naufal.mtoshokan.model.BorrowingBook
import com.dicoding.naufal.mtoshokan.model.User
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import com.dicoding.naufal.mtoshokan.utils.getRemaingDays
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class DashboardViewModel : BaseViewModel() {
    var fullNameLiveData: MutableLiveData<String> = MutableLiveData()
    var imageUrlLiveData: MutableLiveData<String> = MutableLiveData()
    var borrowingLiveData: MutableLiveData<MutableList<BorrowingBook>> = MutableLiveData()
    var borrowedLiveData: MutableLiveData<MutableList<BorrowingBook>> = MutableLiveData()
    var pinaltyLiveData: MutableLiveData<Int> = MutableLiveData()

    init {
        fullNameLiveData.value = auth.currentUser?.displayName
        imageUrlLiveData.value = auth.currentUser?.photoUrl?.toString()
        getData()
    }

    fun getData() {
        scope.launch {
            loading.value = true
            val list = getBorrowing()
            borrowingLiveData.value = list?.filter {
                it.isReturned == false
            }?.toMutableList()

            borrowedLiveData.value = list?.filter {
                it.isReturned == true
            }?.toMutableList()

            pinaltyLiveData.value = getPinalty()

            loading.value = false
        }
    }

    private suspend fun getBorrowing(): MutableList<BorrowingBook>? {
        return withContext(Dispatchers.IO) {
            try {
                val userRef =
                    database.collection(ConstantValue.Database.Users).document(auth.currentUser?.uid.toString())
                val snapshot =
                    database.collection(ConstantValue.Database.BorrowingBooks).whereEqualTo("borrower", userRef)
                        .orderBy("borrowingDate", Query.Direction.DESCENDING).get().await()
                val list = mutableListOf<BorrowingBook>()
                snapshot.forEach { snap ->
                    val bookSnapshot = snap.getString("bookId")?.let { it1 ->
                        database.collection(ConstantValue.Database.Books).document(
                            it1
                        )
                    }?.get()?.await()

                    val bookType =
                        bookSnapshot?.getDocumentReference("bookType")?.get()?.await()?.toObject(BookType::class.java)

                    val book = Book.toBook(bookSnapshot, bookType)

                    val borrowingBook =
                        BorrowingBook.toBorrowingBook(snap, userRef.get().await().toObject(User::class.java), book)
                    list.add(borrowingBook)
                }

                list
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Error => ", e.localizedMessage)
                null
            }
        }
    }

    private suspend fun getPinalty(): Int {
        return withContext(Dispatchers.IO) {
            try {
                var pinalty = 0
                borrowingLiveData.value?.map {
                    val diff = it.returningDate?.getRemaingDays()
                    diff?.let{
                        if(it < 0){
                            pinalty = it.times(5000).times(-1)
                        } else {
                            pinalty = 0
                        }
                    }
                }
                pinalty
            } catch (e: Exception) {
                e.printStackTrace()
                0
            }
        }
    }
}