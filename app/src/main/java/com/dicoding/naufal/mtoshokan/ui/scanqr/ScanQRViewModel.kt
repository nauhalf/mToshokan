package com.dicoding.naufal.mtoshokan.ui.scanqr

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.BookType
import com.dicoding.naufal.mtoshokan.model.Collection
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ScanQRViewModel : BaseViewModel() {
    val flashLiveData: MutableLiveData<Boolean> = MutableLiveData()
    init {
        flashLiveData.value = false
    }

    //-1 = Unavailable, -2 = Error, -99 error
    fun checkBook(isNetwork: Boolean, collectionId: String?, success: (Collection) -> Unit, failed: (Int) -> Unit) {
        if (isNetwork) {
            scope.launch {
                loading.value = true
                collectionId?.let {
                    val c = findCollection(it)
                    c?.let { colle ->

                        loading.value = false
                        colle.available?.let {
                            if (it) {
                                success(c)
                            } else {
                                failed(-1)
                            }
                        } ?: run {
                            failed(-2)
                        }
                    }
                }
                loading.value = false
            }
        } else {
            failed(-99)
        }
    }

    private suspend fun findCollection(collectionId: String): Collection? {
        return withContext(Dispatchers.IO) {
            try {
                val taskSnapshot = database.collection(ConstantValue.Database.Collections).document(collectionId)
                    .get()
                val snapshot = taskSnapshot.await()
                val bookSnapshot = snapshot.getDocumentReference("book")?.get()?.await()
                val bookType =
                    bookSnapshot?.getDocumentReference("bookType")?.get()?.await()?.toObject(BookType::class.java)

                val book = bookType?.let {
                    Book.toBook(bookSnapshot, it)
                }

                val collection = book?.let{
                    Collection.toCollection(snapshot, it)
                }
                collection
            } catch (e: Exception) {
                Log.e("Error => ", e.localizedMessage)
                null
            }

        }
    }
}