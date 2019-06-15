package com.dicoding.naufal.mtoshokan.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.dicoding.naufal.mtoshokan.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var navController: NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUp()

    }

    private fun setUp(){
        navController = findNavController(R.id.main_navigation_fragment)
        navController?.let {
            NavigationUI.setupWithNavController(bottom_nav_main, it)
            it.popBackStack(R.id.bookmarkFragment, true)

            it.popBackStack(R.id.profileFragment, true)
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController?.navigateUp()!!
    }

    companion object {
        fun newIntent(context: Context) : Intent{
            return Intent(context, MainActivity::class.java)
        }
    }
}
