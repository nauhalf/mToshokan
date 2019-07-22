package com.dicoding.naufal.mtoshokan.ui.bookmarkbook

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.BookType
import com.dicoding.naufal.mtoshokan.model.Bookmark
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class BookmarkBookViewModel : BaseViewModel(){
    val bookmarkLiveData: MutableLiveData<MutableList<Bookmark>> = MutableLiveData()
    val bookLiveData: MutableLiveData<Book> = MutableLiveData()

    fun getData(bookId: String){
        scope.launch {
            loading.value = true
            bookLiveData.value = getBook(bookId)
            bookmarkLiveData.value = getBookmark(bookId)
            loading.value =  false
        }
    }

    private suspend fun getBookmark(bookId: String): MutableList<Bookmark>?{
        return withContext(Dispatchers.IO){
            try {
                val bookmarkSnapshot = database.collection(ConstantValue.Database.Bookmarks).whereEqualTo("bookId", bookId).whereEqualTo("userId", auth.currentUser?.uid).get().await()
                val list = mutableListOf<Bookmark>()
                bookmarkSnapshot.map {
                    list.add(Bookmark.toBookmark(it))
                }
                list
            }catch (e: Exception){
                e.printStackTrace()
                Log.e("Error => ", e.localizedMessage)
                null
            }
        }
    }

    private suspend fun getBook(bookId: String): Book?{
        return withContext(Dispatchers.IO){
            try {
                val bookSnapshot = database.document("${ConstantValue.Database.Books}/${bookId}").get().await()
                val bookType = bookSnapshot.getDocumentReference("bookType")?.get()?.await()?.toObject(BookType::class.java)
                Book.toBook(bookSnapshot, bookType)
            }catch (e:Exception){
                e.printStackTrace()
                Log.e("Error => ", e.localizedMessage)
                null
            }
        }
    }
}