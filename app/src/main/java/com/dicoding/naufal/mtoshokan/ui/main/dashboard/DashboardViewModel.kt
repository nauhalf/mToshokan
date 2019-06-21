package com.dicoding.naufal.mtoshokan.ui.main.dashboard

import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel

class DashboardViewModel : BaseViewModel() {
    var fullNameLiveData: MutableLiveData<String> = MutableLiveData()
    var imageUrlLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        fullNameLiveData.value = auth.currentUser?.displayName
        imageUrlLiveData.value = auth.currentUser?.photoUrl?.toString()
    }
}