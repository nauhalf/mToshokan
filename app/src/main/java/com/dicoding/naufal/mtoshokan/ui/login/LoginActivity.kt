package com.dicoding.naufal.mtoshokan.ui.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseViewModel
import com.dicoding.naufal.mtoshokan.databinding.ActivityLoginBinding
import com.dicoding.naufal.mtoshokan.ui.main.MainActivity
import com.dicoding.naufal.mtoshokan.ui.reset.ResetActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var viewmodel: LoginViewModel
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setUp()
    }


    fun setUp(){
        viewmodel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.loginViewModel = viewmodel
        binding.lifecycleOwner = this


        viewmodel.errorLogin.observe(this, Observer {
            if(!it.isNullOrEmpty()){
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


    companion object{
        fun newIntent(context: Context) : Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }
}
