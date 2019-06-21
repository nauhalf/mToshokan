package com.dicoding.naufal.mtoshokan.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.SearchHistory
import com.dicoding.naufal.mtoshokan.utils.ConstantValue
import com.dicoding.naufal.mtoshokan.utils.setting
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SearchViewModel : BaseViewModel() {
    val searchHistoryList: MutableLiveData<MutableList<SearchHistory>> = MutableLiveData()
    private val user = auth.currentUser?.uid

    init {
        searchHistoryList.value = mutableListOf()

    }

    fun search(query: String?) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val ref = database.collection(ConstantValue.Database.SearchHistories).document()
                ref.set(SearchHistory(ref.id, query, null, user)).await()
            }
        }
    }

    fun getHistory() {
        try {
            scope.launch {
                val result = mutableListOf<SearchHistory>()
                withContext(Dispatchers.IO) {
                    val searchHistory =
                        database.collection(ConstantValue.Database.SearchHistories).whereEqualTo("userId", user)
                    val snapshot = searchHistory.get().await()
                    snapshot.forEach {
                        result.add(it.toObject(SearchHistory::class.java))
                    }
                }
                searchHistoryList.value = result
            }
        } catch (e: Exception) {
            Log.d("ERROR: ", e.localizedMessage)
        }
    }

    fun deleteHistory(s: SearchHistory) {
        scope.launch {
            withContext(Dispatchers.IO) {
                s.id?.let {
                    val ref = database.collection(ConstantValue.Database.SearchHistories).document(it)
                    ref.delete().await()
                }

            }
        }
    }
}
