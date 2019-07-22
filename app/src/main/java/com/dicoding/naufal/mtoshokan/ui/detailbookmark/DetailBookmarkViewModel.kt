package com.dicoding.naufal.mtoshokan.ui.detailbookmark

import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.Bookmark

class DetailBookmarkViewModel : BaseViewModel(){
    val bookmarkLiveData: MutableLiveData<Bookmark> = MutableLiveData()
}