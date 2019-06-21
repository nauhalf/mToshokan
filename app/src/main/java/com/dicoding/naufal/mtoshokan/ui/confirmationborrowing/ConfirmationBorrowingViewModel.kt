package com.dicoding.naufal.mtoshokan.ui.confirmationborrowing

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.Collection
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import com.dicoding.naufal.mtoshokan.utils.getNextTenDays
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ConfirmationBorrowingViewModel : BaseViewModel() {


    val collectionLiveData: MutableLiveData<Collection> = MutableLiveData()


    fun borrow(isNetworkConnected: Boolean, success: () -> Unit, failed: (Int) -> Unit) {
        if (isNetworkConnected) {
            scope.launch {
                loading.value = true
                if (dbBorrow()) {
                    success()
                } else {
                    failed(-1)
                }
                loading.value = false
            }
        } else {
            failed(-99)
        }
    }

    suspend fun dbBorrow(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val newBorrowing = database.collection(ConstantValue.Database.BorrowingBooks).document()
                val user = database.document("users/${auth.currentUser?.uid.toString()}")
                val insert = newBorrowing.set(HashMap<String, Any?>().apply {
                    put("bookId", collectionLiveData.value?.book?.bookId)
                    put("borrowingId", newBorrowing.id)
                    put("borrowingDate", FieldValue.serverTimestamp())
                    put("returningDate", getNextTenDays(14))
                    put("isReturned", false)
                    put("user", user)
                }).await()

                val update = collectionLiveData.value?.collectionId?.let {
                    database.collection(ConstantValue.Database.Collections).document(
                        it
                    )
                }

                update?.update(HashMap<String, Any?>().apply{
                    put("available", false)
                })?.await()

                true
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("Error => ", e.localizedMessage)
                false
            }
        }
    }
}