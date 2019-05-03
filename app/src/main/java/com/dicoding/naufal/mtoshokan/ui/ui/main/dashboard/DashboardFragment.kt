package com.dicoding.naufal.mtoshokan.ui.ui.main.dashboard


import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.itemdecoration.MarginItemHorizontalDecoration
import com.dicoding.naufal.mtoshokan.ui.model.borrowingBookList
import com.dicoding.naufal.mtoshokan.ui.ui.book.BookActivity
import com.dicoding.naufal.mtoshokan.ui.ui.borrowing.BorrowingActivity
import com.dicoding.naufal.mtoshokan.ui.ui.main.dashboard.adapter.HistoryBorrowedAdapter
import com.dicoding.naufal.mtoshokan.ui.ui.main.dashboard.adapter.StillBorrowingAdapter
import com.dicoding.naufal.mtoshokan.ui.ui.notification.NotificationActivity
import com.dicoding.naufal.mtoshokan.ui.ui.search.SearchActivity
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    var notificationIcon: Drawable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_dashboard, container, false)

        setHasOptionsMenu(true)
        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notification, menu)
        super.onCreateOptionsMenu(menu, inflater)
        notificationIcon = menu.getItem(0).icon
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_notification -> {
                val intent = Intent(context, NotificationActivity::class.java)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setUp() {
        val a = (activity as AppCompatActivity)
        a.setSupportActionBar(toolbar)
        a.supportActionBar?.title = ""
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (collapseToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapseToolbar)) {
                notificationIcon?.setColorFilter(
                    ContextCompat.getColor(a, R.color.colorPrimary),
                    PorterDuff.Mode.SRC_ATOP
                )
                ImageViewCompat.setImageTintList(img_logo, ContextCompat.getColorStateList(a, R.color.colorPrimary))
            } else {
                notificationIcon?.setColorFilter(ContextCompat.getColor(a, R.color.white), PorterDuff.Mode.SRC_ATOP)
                toolbar.navigationIcon?.setColorFilter(
                    ContextCompat.getColor(a, R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
                ImageViewCompat.setImageTintList(img_logo, ContextCompat.getColorStateList(a, R.color.white))
            }
        })

        btnSearch.setOnClickListener {
            val searchIntent = Intent(activity, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        recycler_history_borrowed.apply {
            adapter = HistoryBorrowedAdapter(
                borrowingBookList.filter {
                    it.isReturned == true
                }.toMutableList()
            ) {
                startActivity(BookActivity.newIntent(context, it))
            }

            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(MarginItemHorizontalDecoration(context.resources.getDimension(R.dimen.card_horizontal_margin)))
        }

        recycler_still_borrowing.apply {
            adapter = StillBorrowingAdapter(
                borrowingBookList.filter {
                    it.isReturned == false
                }.toMutableList()
            ){
                startActivity(BorrowingActivity.newIntent(context, it))
            }
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(MarginItemHorizontalDecoration(context.resources.getDimension(R.dimen.card_horizontal_margin)))
        }
    }
}
