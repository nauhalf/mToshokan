package com.dicoding.naufal.mtoshokan.ui.main.profile.password

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityEditPasswordBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_edit_password.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class EditPasswordActivity : BaseActivity<ActivityEditPasswordBinding, EditPasswordViewModel>() {

    lateinit var mEditPasswordViewModel: EditPasswordViewModel
    lateinit var binding: ActivityEditPasswordBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_password
    }

    override fun getViewModel(): EditPasswordViewModel {
        mEditPasswordViewModel = ViewModelProviders.of(this).get(EditPasswordViewModel::class.java)
        return mEditPasswordViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.editPasswordViewModel
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

    fun setUp() {

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_save.setOnClickListener {
            if (validateNewPassword()) {
                mEditPasswordViewModel.save(isNetworkConnected,
                    {
                        setResult(Activity.RESULT_OK)
                        finish()

                    }, { errorCode ->
                        when (errorCode) {
                            -1 -> {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Kata sandi lama salah",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                            -2 -> {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    "Terjadi kesalahan ketika mengubah kata sandi",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                            99 -> {
                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    getString(R.string.offline_network),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        }
    }

    private fun validateNewPassword(): Boolean {
        mEditPasswordViewModel.newPasswordLiveData.value.let {
            mEditPasswordViewModel.errorNewPasswordLiveData.value = if (it.isNullOrEmpty() || it.length < 6) {
                "Kata sandi tidak boleh kurang dari 6 karakter"
            } else {
                null
            }
        }

        mEditPasswordViewModel.retypePasswordLiveData.value.let {
            mEditPasswordViewModel.errorRetypePasswordLiveData.value =
                if (!it.equals(mEditPasswordViewModel.newPasswordLiveData.value)) {
                    "Kata sandi lama dan baru tidak sama"
                } else {
                    null
                }
        }

        return mEditPasswordViewModel.errorNewPasswordLiveData.value.isNullOrEmpty() && mEditPasswordViewModel.errorRetypePasswordLiveData.value.isNullOrEmpty()
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, EditPasswordActivity::class.java)
    }
}
