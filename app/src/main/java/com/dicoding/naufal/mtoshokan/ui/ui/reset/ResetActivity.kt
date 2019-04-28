package com.dicoding.naufal.mtoshokan.ui.ui.reset

import android.os.Bundle
import android.app.Activity
import android.content.Context
import android.content.Intent
import com.dicoding.naufal.mtoshokan.R

import kotlinx.android.synthetic.main.activity_reset.*

class ResetActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        setUp()
    }

    private fun setUp(){
        btnBack.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun newIntent(context:Context): Intent {
            return Intent(context, ResetActivity::class.java)
        }
    }

}
