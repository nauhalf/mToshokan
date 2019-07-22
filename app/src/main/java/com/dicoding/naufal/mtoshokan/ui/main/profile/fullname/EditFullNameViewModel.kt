package com.dicoding.naufal.mtoshokan.ui.main.profile.fullname

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditFullNameViewModel : BaseViewModel() {
    var fullNameLiveData: MutableLiveData<String> = MutableLiveData()

    var errorFullNameLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        loading.value = false
    }

    fun save(isNetworkConnected: Boolean, success: () -> Unit, failed: (Int) -> Unit) {
        if(isNetworkConnected){
            scope.launch {
                loading.value = true
                if (update()) {
                    loading.value = false
                    success()
                } else {
                    loading.value = false
                    failed(-1)
                }
            }
        }else {
            failed(-99)
        }
    }

    private suspend fun update(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                auth.currentUser?.uid?.let {
                    database.collection(ConstantValue.Database.Users).document(
                        it
                    ).update("userFullName", fullNameLiveData.value)
                }?.await()

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(fullNameLiveData.value)
                    .build()
                auth.currentUser?.updateProfile(profileUpdates)?.await()

                true

            } catch (e: Exception) {
                Log.d("Error => ", e.localizedMessage)
                false
            }
        }
    }

}