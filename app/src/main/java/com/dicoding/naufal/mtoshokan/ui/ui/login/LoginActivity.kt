package com.dicoding.naufal.mtoshokan.ui.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.ui.main.MainActivity
import com.dicoding.naufal.mtoshokan.ui.ui.reset.ResetActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUp()
    }

    private fun setUp(){
        txtResetPassword.setOnClickListener {
            startActivity(ResetActivity.newIntent(this))
        }

        btnLogin.setOnClickListener {
            startActivity(MainActivity.newIntent(this))
        }
    }
}
