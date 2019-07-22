package com.dicoding.naufal.mtoshokan.ui.addbookmark

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.Bookmark
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import com.dicoding.naufal.mtoshokan.utils.update
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class AddBookmarkViewModel : BaseViewModel() {
    val bookLiveData: MutableLiveData<Book> = MutableLiveData()
    val imageLiveData: MutableLiveData<MutableList<Uri>> = MutableLiveData()
    val bookmarkTitleLiveData: MutableLiveData<String> = MutableLiveData()
    val errorBookmarkTitleLiveData: MutableLiveData<String> = MutableLiveData()
    val bookmarkDescriptionLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        imageLiveData.value = mutableListOf()
    }

    fun addImage(list: List<Uri>) {
        scope.launch {
            imageLiveData.value?.addAll(list)
            imageLiveData.update()
        }
    }

    fun delete(index: Int) {
        scope.launch {
            imageLiveData.value?.removeAt(index)
            imageLiveData.update()
        }
    }

    fun save(isNetworkConnected: Boolean, success: () -> Unit, failed: (Int) -> Unit) {
        if (isNetworkConnected) {
            scope.launch {
                loading.value = true

                val link = upload()
                if (link != null) {
                    val s = savedb(link)
                    if (s == true) {
                        success()
                    } else {
                        failed(-2)
                    }
                } else {
                    failed(-1)
                }

                loading.value = false
            }
        } else {
            failed(-99)
        }
    }

    private suspend fun upload(): List<String>? {
        return withContext(Dispatchers.IO) {
            try {
                val files = imageLiveData.value
                val link = files?.map { uri ->
                    val fileName = uri.lastPathSegment

                    val fileRef =
                        FirebaseStorage.getInstance()
                            .reference.child("${ConstantValue.Storage.BookmarkImage}/${UUID.randomUUID()}")
                    fileRef.putFile(uri).await()
                    val link = fileRef.downloadUrl.await()
                    link.toString()
                }
                link
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Error => ", e.localizedMessage)
                null
            }
        }
    }

    private suspend fun savedb(links: List<String>?): Boolean? {
        return withContext(Dispatchers.IO) {
            try {
                links?.let {
                    val bookmarksRef = database.collection(ConstantValue.Database.Bookmarks).document()
                    bookmarksRef.set(
                        Bookmark(
                            bookmarkId = bookmarksRef.id,
                            bookmarkTitle = bookmarkTitleLiveData.value,
                            bookmarkDescription = bookmarkDescriptionLiveData.value,
                            bookmarkImages = it,
                            userId = auth.currentUser?.uid,
                            bookId = bookLiveData.value?.bookId,
                            created = null
                        )
                    ).await()
                    true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Error => ", e.localizedMessage)
                false
            }
        }
    }
}