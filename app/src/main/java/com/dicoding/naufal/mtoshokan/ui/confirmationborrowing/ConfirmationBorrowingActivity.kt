package com.dicoding.naufal.mtoshokan.ui.confirmationborrowing

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityConfirmationBorrowingBinding
import com.dicoding.naufal.mtoshokan.model.Collection
import com.dicoding.naufal.mtoshokan.utils.ProgressDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_confirmation_borrowing.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class ConfirmationBorrowingActivity :
    BaseActivity<ActivityConfirmationBorrowingBinding, ConfirmationBorrowingViewModel>() {

    private lateinit var mConfirmationBorrowingViewModel: ConfirmationBorrowingViewModel
    private lateinit var binding: ActivityConfirmationBorrowingBinding
    private lateinit var dialog: AlertDialog
    override fun getLayoutId(): Int {
        return R.layout.activity_confirmation_borrowing
    }

    override fun getViewModel(): ConfirmationBorrowingViewModel {
        mConfirmationBorrowingViewModel = ViewModelProviders.of(this).get(ConfirmationBorrowingViewModel::class.java)
        return mConfirmationBorrowingViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.confirmationBorrowingViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setShowHideAnimationEnabled(true)

        dialog = ProgressDialog.build(this)

        btn_ok.setOnClickListener {
            mConfirmationBorrowingViewModel.borrow(isNetworkConnected, {

                ProgressDialog.buildOk(this, "Buku berhasil dipinjam") {
                    setResult(Activity.RESULT_OK)
                    finish()
                }.show()

            }, {
                when(it){
                    -1 -> {
                        Snackbar.make(root, "Gagal dalam melakukan peminjaman buku", Snackbar.LENGTH_SHORT).show()
                    }
                    -99 -> {
                        Snackbar.make(root, getString(R.string.offline_network), Snackbar.LENGTH_SHORT).show()

                    }
                }
            })
        }

        btn_cancel.setOnClickListener {
            finish()
        }
        mConfirmationBorrowingViewModel.collectionLiveData.value = intent.getParcelableExtra("collection")
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mConfirmationBorrowingViewModel.collectionLiveData.observe(this, Observer {
            it.book?.bookCover?.let {url->
                if (url.isNotEmpty()) {
                    Glide.with(this)
                        .load(url)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .into(img_cover)
                }
            }
        })

        mConfirmationBorrowingViewModel.loading.observe(this, Observer {
            if(it){
                dialog.show()
            } else {
                dialog.dismiss()
            }
        })
    }

    companion object {
        fun newIntent(context: Context, collection: Collection): Intent =
            Intent(context, ConfirmationBorrowingActivity::class.java).apply {
                putExtra("collection", collection)
            }
    }
}
