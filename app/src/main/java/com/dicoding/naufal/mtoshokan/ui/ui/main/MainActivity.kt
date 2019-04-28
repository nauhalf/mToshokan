package com.dicoding.naufal.mtoshokan.ui.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.naufal.mtoshokan.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUp()
//        recycler_history_borrowed.apply {
//            adapter = HistoryBorrowedAdapter(mutableListOf(Book("Hyouka", "AA", "AA"),
//                Book("Hyouka 2 Credit Roll Of The Fool", "AA", "AA"),
//                Book("Hyouka 3 Kudryavka Sequence", "AA", "AA")))
//            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
//            addItemDecoration(MarginItemHorizontalDecoration(context.resources.getDimension(R.dimen.card_horizontal_margin)))
//        }
    }

    private fun setUp(){
        val navController = findNavController(R.id.main_navigation_fragment)
        bottom_nav_main.setupWithNavController(navController)
    }

    override fun onNavigateUp(): Boolean {
        return findNavController(R.id.main_navigation_fragment).navigateUp()
    }
}
