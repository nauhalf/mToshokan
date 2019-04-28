package com.dicoding.naufal.mtoshokan.ui.ui.main.dashboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.itemdecoration.MarginItemHorizontalDecoration
import com.dicoding.naufal.mtoshokan.ui.model.BorrowingBook
import com.dicoding.naufal.mtoshokan.ui.model.borrowingBookList
import com.dicoding.naufal.mtoshokan.ui.ui.main.adapter.HistoryBorrowedAdapter
import com.dicoding.naufal.mtoshokan.ui.ui.main.adapter.StillBorrowingAdapter
import com.dicoding.naufal.mtoshokan.ui.utils.getNextTenDays
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.util.*

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    fun setUp(){
        recycler_history_borrowed.apply {
            adapter = HistoryBorrowedAdapter(
                borrowingBookList.filter {
                    it.isReturned == true
                }.toMutableList()
            )
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(MarginItemHorizontalDecoration(context.resources.getDimension(R.dimen.card_horizontal_margin)))
        }

        recycler_still_borrowing.apply {
            adapter = StillBorrowingAdapter(
                borrowingBookList.filter {
                    it.isReturned == false
                }.toMutableList()
            )
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(MarginItemHorizontalDecoration(context.resources.getDimension(R.dimen.card_horizontal_margin)))
        }

        Toast.makeText(activity, getNextTenDays(0).toString() + " - " + getNextTenDays(10).toString(), Toast.LENGTH_LONG).show()
    }
}
