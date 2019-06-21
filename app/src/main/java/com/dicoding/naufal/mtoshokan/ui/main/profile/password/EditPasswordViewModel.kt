package com.dicoding.naufal.mtoshokan.ui.main.profile.password

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditPasswordViewModel : BaseViewModel() {


    val oldPasswordLiveData: MutableLiveData<String> = MutableLiveData()
    val newPasswordLiveData: MutableLiveData<String> = MutableLiveData()
    val retypePasswordLiveData: MutableLiveData<String> = MutableLiveData()

    val errorOldPasswordLiveData: MutableLiveData<String> = MutableLiveData()
    val errorNewPasswordLiveData: MutableLiveData<String> = MutableLiveData()
    val errorRetypePasswordLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        loading.value = false
    }

    fun save(isNetworkConnected: Boolean, success: () -> Unit, failed: (Int) -> Unit) {
        if (isNetworkConnected) {
            scope.launch {
                loading.value = true
                if (validationOldPassword()) {

                    errorOldPasswordLiveData.value = null

                    if (update()) {
                        success()
                    } else {
                        loading.value = false
                        failed(-2)
                    }
                } else {
                    loading.value = false
                    failed(-1)
                }
            }
        } else {
            failed(-99)
        }
    }

    private suspend fun update(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                newPasswordLiveData.value?.let {
                    val update = auth.currentUser?.updatePassword(it)?.await()

                }
                true

            } catch (e: Exception) {
                Log.d("Error => ", e.localizedMessage)
                false
            }
        }
    }

    private suspend fun validationOldPassword(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val credential = oldPasswordLiveData.value?.let { old ->
                    auth.currentUser?.email?.let { EmailAuthProvider.getCredential(it, old) }
                }

                // Prompt the user to re-provide their sign-in credentials
                credential?.let { auth.currentUser?.reauthenticate(it)?.await() }
                true

            } catch (e: Exception) {
                Log.d("Error => ", e.localizedMessage)
                false
            }
        }
    }
}