package com.dicoding.naufal.mtoshokan.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.naufal.mtoshokan.utils.setting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel(){

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    protected val scope = CoroutineScope(coroutineContext)

    val loading: MutableLiveData<Boolean> = MutableLiveData()

    protected val database = FirebaseFirestore.getInstance()
    protected val auth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        database.firestoreSettings = setting
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}