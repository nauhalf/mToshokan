package com.dicoding.naufal.mtoshokan.ui.manualqr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityManualQrBinding
import com.dicoding.naufal.mtoshokan.utils.ProgressDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_manual_qr.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class ManualQRActivity : BaseActivity<ActivityManualQrBinding, ManualQRViewModel>() {

    lateinit var mManualQRViewModel: ManualQRViewModel
    lateinit var binding: ActivityManualQrBinding
    lateinit var dialog: AlertDialog

    override fun getLayoutId(): Int {
        return R.layout.activity_manual_qr
    }

    override fun getViewModel(): ManualQRViewModel {
        mManualQRViewModel = ViewModelProviders.of(this).get(ManualQRViewModel::class.java)
        return mManualQRViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.manualQRViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {
        dialog = ProgressDialog.build(this)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        btn_save.setOnClickListener {
            hideKeyboard()
            mManualQRViewModel.checkBook(true, {
                setResult(Activity.RESULT_OK, Intent().putExtra("collection", it))
                finish()
            }, {
                when (it) {
                    -1 -> {
                        Snackbar.make(root, "Buku tidak tersedia", Snackbar.LENGTH_SHORT).show()
                    }
                    -2 -> {
                        Snackbar.make(root, "Gagal mendapatkan informasi", Snackbar.LENGTH_SHORT).show()

                    }
                    -99 -> {
                        Snackbar.make(root, getString(R.string.offline_network), Snackbar.LENGTH_SHORT).show()
                    }
                }
            })
        }
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mManualQRViewModel.loading.observe(this, Observer {
            if (it) {
                dialog.show()
            } else {
                dialog.dismiss()
            }
        })
    }

    companion object{
        fun newIntent(context:Context) : Intent = Intent(context, ManualQRActivity::class.java)
    }
}
