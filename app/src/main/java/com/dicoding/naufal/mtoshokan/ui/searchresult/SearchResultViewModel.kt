package com.dicoding.naufal.mtoshokan.ui.searchresult

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.BookType
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import com.dicoding.naufal.mtoshokan.utils.setting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SearchResultViewModel : BaseViewModel() {

    val bookList: MutableLiveData<MutableList<Book>> = MutableLiveData()
    val querySearch: MutableLiveData<String> = MutableLiveData()

    init {
        loading.value = true
    }

    fun loadSearchData(query: String) {
        try {
            scope.launch {
                loading.value = true
                querySearch.value = query
                val result = mutableListOf<Book>()
                val filter = mutableListOf<Book>()
                withContext(Dispatchers.IO) {
                    val ref = database.collection(ConstantValue.Database.Books).orderBy("bookTitle").get()
                    val snapshot = ref.await()
                    snapshot.forEach {
                        val bookType = it.getDocumentReference("bookType")?.get()
                        val book = Book(
                            bookId = it.getString("bookId"),
                            bookCover = it.getString("bookCover"),
                            bookISBN = it.getString("bookISBN"),
                            bookLocation = it.getString("bookLocation"),
                            bookPublisher = it.getString("bookPublisher"),
                            bookSynopsis = it.getString("bookSynopsis"),
                            bookTitle = it.getString("bookTitle"),
                            bookWriter = it.getString("bookWriter"),
                            bookType = bookType?.await()?.toObject(BookType::class.java)
                        )
                        result.add(book)
                    }

                    if (query.isNotEmpty()) {
                        val q = query.toLowerCase()
                        filter.addAll(result.filter {book->
                            book.bookTitle!!.toLowerCase().contains(q) ||
                                    book.bookSynopsis!!.toLowerCase().contains(q) ||
                                    book.bookISBN!! == query ||
                                    book.bookWriter!!.toLowerCase().contains(q) ||
                                    book.bookPublisher!!.toLowerCase().contains(q)
                        }.sortedBy { it.bookTitle }.toMutableList())
////                        filter.addAll(result.filter { book ->
////                            book.bookTitle!!.contains(query) ||
////                                    book.bookSynopsis?.contains(query) ||
////                                    book.bookISBN.equals(query) ||
////                                    book.bookPublisher.contains(query) ||
////                                    book.bookWriter.contains(query)
//                        })
                    }

                }
                bookList.value = if(query.isNotEmpty()) filter else result
                loading.value = false
            }
        } catch (e: Exception) {
            Log.d("Error: ", e.localizedMessage)
        }
    }


}