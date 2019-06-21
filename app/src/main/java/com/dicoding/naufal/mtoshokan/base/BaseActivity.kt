package com.dicoding.naufal.mtoshokan.base

import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.dicoding.naufal.mtoshokan.utils.NetworkUtils
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity<T: ViewDataBinding, V: BaseViewModel> : AppCompatActivity(), BaseFragment.Callback {
    private lateinit var mViewDataBinding: T
    private lateinit var mViewModel: V

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): V

    abstract fun getBindingVariable() : Int


    val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    fun getViewDataBinding() : T{
        return mViewDataBinding
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        performDataBinding()
        if(!isNetworkConnected) Snackbar.make(findViewById(android.R.id.content), "Jaringan mati / offline", Snackbar.LENGTH_SHORT).show()
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun hideKeyboard(){
        val view = this.currentFocus
        view?.let {v ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }
    }


    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    private fun performDataBinding(){
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        this.mViewModel = getViewModel()
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel)
        mViewDataBinding.executePendingBindings()
        mViewDataBinding.lifecycleOwner = this
    }
}