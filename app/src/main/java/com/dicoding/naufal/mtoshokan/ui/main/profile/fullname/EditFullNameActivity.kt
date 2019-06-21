package com.dicoding.naufal.mtoshokan.ui.main.profile.fullname

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityEditFullNameBinding
import com.dicoding.naufal.mtoshokan.ui.main.profile.ProfileFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_edit_full_name.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class EditFullNameActivity : BaseActivity<ActivityEditFullNameBinding, EditFullNameViewModel>() {

    lateinit var mEditFullNameViewModel: EditFullNameViewModel
    lateinit var binding: ActivityEditFullNameBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_edit_full_name
    }

    override fun getViewModel(): EditFullNameViewModel {
        mEditFullNameViewModel = ViewModelProviders.of(this).get(EditFullNameViewModel::class.java)
        return mEditFullNameViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.editFullNameViewModel
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


    fun setUp(){

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btn_save.setOnClickListener{
            if(validate()) return@setOnClickListener

            hideKeyboard()
            mEditFullNameViewModel.save(isNetworkConnected, {
                setResult(Activity.RESULT_OK, Intent().putExtra("fullName", mEditFullNameViewModel.fullNameLiveData.value))
                finish()
            },{errorCode ->
                Snackbar.make(findViewById(android.R.id.content), when(errorCode){
                    -1 -> {
                        "Terjadi kesalahan ketika mengubah nama"
                    }
                    else -> {
                        getString(R.string.offline_network)
                    }
                }, Snackbar.LENGTH_SHORT)
                    .show()
            })
        }

        subscribeToLiveData()
    }

    private fun subscribeToLiveData(){
        mEditFullNameViewModel.fullNameLiveData.value = intent.getStringExtra("fullName")

    }

    private fun validate(): Boolean {
        mEditFullNameViewModel.errorFullNameLiveData.value = if (mEditFullNameViewModel.errorFullNameLiveData.value.isNullOrEmpty()) {
            "Nama tidak boleh kosong"
        } else {
                null

        }
        return mEditFullNameViewModel.errorFullNameLiveData.value.isNullOrEmpty()
    }


    companion object{
        fun newIntent(context: Context, fullName: String?) : Intent = Intent(context, EditFullNameActivity::class.java).apply{
            putExtra("fullName", fullName)
        }
    }
}
