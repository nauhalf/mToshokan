package com.dicoding.naufal.mtoshokan.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.naufal.mtoshokan.R
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
    }
}
