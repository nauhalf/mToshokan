package com.dicoding.naufal.mtoshokan.ui.borrowing

import androidx.lifecycle.MutableLiveData
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.model.BorrowingBook

class BorrowingViewModel : BaseViewModel() {

    val borrowingLiveData: MutableLiveData<BorrowingBook> = MutableLiveData()


}