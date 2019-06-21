package com.dicoding.naufal.mtoshokan.ui.scanqr

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PointF
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityScanQrBinding
import com.dicoding.naufal.mtoshokan.model.Collection
import com.dicoding.naufal.mtoshokan.ui.confirmationborrowing.ConfirmationBorrowingActivity
import com.dicoding.naufal.mtoshokan.ui.manualqr.ManualQRActivity
import com.dicoding.naufal.mtoshokan.utils.ProgressDialog
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_scan_qr.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*


class ScanQRActivity : BaseActivity<ActivityScanQrBinding, ScanQRViewModel>(), QRCodeReaderView.OnQRCodeReadListener {

    private lateinit var mScanQRViewModel: ScanQRViewModel
    private lateinit var binding: ActivityScanQrBinding
    private lateinit var dialog: AlertDialog

    private var menuItem: Menu? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_scan_qr
    }

    override fun getViewModel(): ScanQRViewModel {
        mScanQRViewModel = ViewModelProviders.of(this).get(ScanQRViewModel::class.java)
        return mScanQRViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.scanQRViewModel
    }

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        qr.stopCamera()
        mScanQRViewModel.checkBook(isNetworkConnected, text, {
            confirmationBorrow(it)
        },{
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
            qr.startCamera()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        if (savedInstanceState != null) {
            mScanQRViewModel.flashLiveData.value = savedInstanceState.getBoolean("FLASH_STATE")
        }
        setUp()
    }

    override fun onPause() {
        super.onPause()
        qr.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        qr.startCamera()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("FLASH_STATE", mScanQRViewModel.flashLiveData.value!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_flash, menu)
        menuItem = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_flash -> {
                mScanQRViewModel.flashLiveData.value = !mScanQRViewModel.flashLiveData.value!!
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ_INSERT_MANUAL && resultCode == Activity.RESULT_OK){
            confirmationBorrow(data?.getParcelableExtra("collection"))
        } else if(requestCode == REQ_CONFIRMATION_BORROWING) {
            if(resultCode == Activity.RESULT_OK) {
                finish()
            }
        }
    }

    fun confirmationBorrow(collection: Collection?){
        startActivityForResult(collection?.let { ConfirmationBorrowingActivity.newIntent(this, it) }, REQ_CONFIRMATION_BORROWING)
    }

    private fun showMessageDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Scan Results")
            .setMessage(message)
            .create().show()
    }

    fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        dialog = ProgressDialog.build(this)

        qr.setOnQRCodeReadListener(this)
        qr.setBackCamera()
        qr.setAutofocusInterval(5000)

        btn_insert_manual.setOnClickListener {
            startActivityForResult(ManualQRActivity.newIntent(this), REQ_INSERT_MANUAL)
        }
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mScanQRViewModel.flashLiveData.observe(this, Observer {
            if (it) {
                menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_flash_on_white)
                qr.setTorchEnabled(it)
            } else {
                menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_flash_off_white)
                qr.setTorchEnabled(it)
            }
        })

        mScanQRViewModel.loading.observe(this, Observer {
            if(it){
                dialog.show()
            } else {
                dialog.dismiss()
            }
        })
    }

    companion object {

        fun newIntent(context: Context): Intent = Intent(context, ScanQRActivity::class.java)

        const val REQ_INSERT_MANUAL = 1
        const val REQ_CONFIRMATION_BORROWING = 2
    }


}
