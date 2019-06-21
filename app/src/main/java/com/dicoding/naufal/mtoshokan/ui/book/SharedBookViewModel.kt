package com.dicoding.naufal.mtoshokan.ui.book

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.BookType
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SharedBookViewModel : BaseViewModel() {
    val bookLiveData: MutableLiveData<Book> = MutableLiveData()
    val stockLiveData: MutableLiveData<Int> = MutableLiveData()
    init {
        loading.value = true
    }

    fun loadData(bookId: String) {
        try {
            scope.launch {
                loading.value = true

                var i = 0
                val b = withContext(Dispatchers.IO) {
                    val ref = database.collection(ConstantValue.Database.Books).document(bookId)
                    val book = ref.get().await()
                    val bookType = book.getDocumentReference("bookType")?.get()?.await()?.toObject(BookType::class.java)

                    val collection = database.collection(ConstantValue.Database.Collections).whereEqualTo("book", ref)
                            .get()
                    collection.await().forEach {
                        if (it.getBoolean("available")!!) {
                            i++
                        }
                    }
                    bookType?.let { Book.toBook(book, it) }
                }
                bookLiveData.value = b
                stockLiveData.value = i
                loading.value = false

            }
        } catch (e: Exception) {
            Log.d("Error => ", e.localizedMessage)
        }
    }
}