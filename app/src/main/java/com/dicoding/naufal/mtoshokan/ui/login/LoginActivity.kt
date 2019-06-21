package com.dicoding.naufal.mtoshokan.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityLoginBinding
import com.dicoding.naufal.mtoshokan.ui.main.MainActivity
import com.dicoding.naufal.mtoshokan.ui.reset.ResetActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {

    lateinit var auth: FirebaseAuth
    lateinit var viewmodel: LoginViewModel
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        binding = getViewDataBinding()
        setUp()
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun getViewModel(): LoginViewModel {
        viewmodel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        return viewmodel
    }

    override fun getBindingVariable(): Int {
        return BR.loginViewModel
    }

    fun setUp() {

        viewmodel.errorLogin.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        viewmodel.user.observe(this, Observer {
            it.let {
                startActivity(MainActivity.newIntent(this))
                finish()
            }
        })

        txtResetPassword.setOnClickListener {
            startActivity(ResetActivity.newIntent(this))
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        currentUser?.let {
            startActivity(MainActivity.newIntent(this))
            finish()
        }
    }


    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
