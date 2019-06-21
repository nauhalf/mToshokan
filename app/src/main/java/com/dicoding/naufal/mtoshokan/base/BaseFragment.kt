package com.dicoding.naufal.mtoshokan.base

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseFragment<T : ViewDataBinding, V : BaseViewModel> : Fragment() {

    var mBaseActivity: BaseActivity<*, *>? = null
        private set
    private var rootView: View? = null
    lateinit var mViewDataBinding: T
        private set
    private lateinit var viewModel: V

    abstract val bindingVariable: Int

    @get:LayoutRes
    abstract val layoutId: Int

    val isNetworkConnected: Boolean
        get() = mBaseActivity?.isNetworkConnected ?: false

    abstract fun getViewModel(): V


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*, *>) {
            mBaseActivity = context
            context.onFragmentAttached()
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun hasPermission(permission: String): Boolean =
        checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getViewModel()
        setHasOptionsMenu(false)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        rootView = mViewDataBinding.root
        return rootView
    }

    override fun onDetach() {
        mBaseActivity = null
        super.onDetach()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewDataBinding.setVariable(bindingVariable, viewModel)
        mViewDataBinding.lifecycleOwner = viewLifecycleOwner
        mViewDataBinding.executePendingBindings()
    }

    fun hideKeyboard() {
        mBaseActivity?.hideKeyboard()
    }

    fun getBaseActivity(): BaseActivity<*, *>? {
        return mBaseActivity
    }

    fun getViewDataBinding(): T {
        return mViewDataBinding
    }


    protected fun checkPermission(permissions: List<String>, requestCode: Int): Boolean {


        val listPermission = arrayListOf<String>()
        permissions.forEach {
            if (!hasPermission(it)) {
                listPermission.add(it)
            }
        }

        if (listPermission.isNotEmpty()) {
            requestPermissions(listPermission.toTypedArray(), requestCode)
            return false
        }
        return true
    }
    interface Callback {
        fun onFragmentAttached()
        fun onFragmentDetached(tag: String)
    }
}