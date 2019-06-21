package com.dicoding.naufal.mtoshokan.ui.login

import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.utils.isValidEmail
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginViewModel : BaseViewModel() {
    val email: MutableLiveData<String> = MutableLiveData()
    val password: MutableLiveData<String> = MutableLiveData()
    val user: MutableLiveData<FirebaseUser> = MutableLiveData()
    val errorEmail: MutableLiveData<String> = MutableLiveData()
    val errorPassword: MutableLiveData<String> = MutableLiveData()
    val errorLogin: MutableLiveData<String> = MutableLiveData()

    init {
        loading.value = false
    }

    fun login(): Unit {
        scope.launch {
            try {
                if (validate()) {
                    loading.value = true

                    val auth = withContext(Dispatchers.IO) {
                        auth.signInWithEmailAndPassword(email.value!!, password.value!!)
                    }.await()


                    user.value = auth.user
                }
            } catch (e: Exception) {
                errorLogin.value = if (e is FirebaseAuthInvalidCredentialsException) {
                    "Email / Password yang anda masukkan salah"
                } else if (e is FirebaseAuthInvalidUserException) {
                    "Email tidak terdaftar"
                } else {
                    e.localizedMessage
                }
            } finally {
                loading.value = false
            }

        }
    }

    private fun validate(): Boolean {
        errorEmail.value = if (email.value.isNullOrEmpty()) {
            "Email tidak boleh kosong"
        } else {
            if (!email.value.isValidEmail()) {
                "Format email salah"
            } else {
                null
            }
        }

        errorPassword.value = if (password.value.isNullOrEmpty()) {
            "Password tidak boleh kosong"
        } else {
            null
        }

        return errorEmail.value.isNullOrEmpty() || errorPassword.value.isNullOrEmpty()
    }
}