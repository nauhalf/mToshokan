package com.dicoding.naufal.mtoshokan.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.User
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import com.dicoding.naufal.mtoshokan.utils.update
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileViewModel : BaseViewModel() {
    var profileLiveData: MutableLiveData<User> = MutableLiveData()

    init {
        loading.value = true
        loadData()
    }

    fun loadData() {
        try {
            scope.launch {
                val user = withContext(Dispatchers.IO) {
                    val ref =
                        auth.currentUser?.uid?.let { database.collection(ConstantValue.Database.Users).document(it) }

                    val user = ref?.get()?.await()
                    user.let {
                        user?.toObject(User::class.java)

                    } ?: run {
                        val newUser = auth.currentUser?.uid?.let {
                            database.collection(ConstantValue.Database.Users).document(it)
                        }
                        val u = User(
                            newUser?.id,
                            "",
                            auth.currentUser?.email,
                            ""
                        )
                        val insert = newUser?.set(u)?.await()
                        u
                    }

                }
                profileLiveData.value = user
                loading.value = false
            }
        } catch (e: Exception) {
            Log.d("Error => ", e.localizedMessage)
            loading.value = false
        }
    }

    fun deleteUserPhoto(callback: ()-> Unit) {
        scope.launch {
            deletePhotoFromDb(auth.currentUser?.photoUrl.toString())
            profileLiveData.value?.userPhoto = null
            profileLiveData.update()
            callback()
        }
    }

    private suspend fun deletePhotoFromDb(url: String) {
        withContext(Dispatchers.IO) {
            try {
                // Create a storage reference from our app
                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
                val storageDeleteRef = storageRef.delete()

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(null)
                    .build()
                val authDeleteRef = auth.currentUser?.updateProfile(profileUpdates)

                val dbDeleteRef = auth.currentUser?.uid?.let {
                    database.collection(ConstantValue.Database.Users)
                        .document(it)
                        .update("userPhoto", null)
                }
                storageDeleteRef.await()
                authDeleteRef?.await()
                dbDeleteRef?.await()

            } catch (e: Exception) {
                Log.d("Error => ", e.localizedMessage)
            }
        }
    }

    fun uploadImage(uri: Uri?, success: (Uri?) -> Unit, failed: () -> Unit) {
        scope.launch {
            if (uploadImageDb(uri) != null) {
                success(uri)
            } else {
                failed()
            }
        }
    }

    suspend fun uploadImageDb(uri: Uri?): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val file = uri
                val fileName =
                    uri?.lastPathSegment
                val fileRef =
                    FirebaseStorage.getInstance().reference.child("${ConstantValue.Storage.UserPhoto}/$fileName")
                val uploadTask = file?.let { fileRef.putFile(it) }?.await()
                val successUrl = fileRef.downloadUrl.await()

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(successUrl)
                    .build()
                val authPhotoTask = auth.currentUser?.updateProfile(profileUpdates)

                val dbPhotoTask = auth.currentUser?.uid?.let {
                    database.collection(ConstantValue.Database.Users)
                        .document(it)
                        .update("userPhoto", successUrl.toString())
                }

                authPhotoTask?.await()
                dbPhotoTask?.await()
                successUrl
            } catch (e: java.lang.Exception) {
                Log.e("Error => ", e.localizedMessage)
                null
            }
        }
    }
}