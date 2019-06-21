package com.dicoding.naufal.mtoshokan.ui.main.dashboard


import android.Manifest
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseFragment
import com.dicoding.naufal.mtoshokan.databinding.FragmentDashboardBinding
import com.dicoding.naufal.mtoshokan.ui.notification.NotificationActivity
import com.dicoding.naufal.mtoshokan.ui.scanqr.ScanQRActivity
import com.dicoding.naufal.mtoshokan.ui.search.SearchActivity
import com.dicoding.naufal.mtoshokan.utils.showPermissionErrorMessage
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment<FragmentDashboardBinding, DashboardViewModel>() {

    lateinit var mDashboardViewModel: DashboardViewModel
    lateinit var binding: FragmentDashboardBinding

    private var notificationIcon: Drawable? = null

    override val bindingVariable: Int
        get() = BR.dashboardViewModel

    override val layoutId: Int
        get() = R.layout.fragment_dashboard

    override fun getViewModel(): DashboardViewModel {
        mDashboardViewModel = ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        return mDashboardViewModel
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_notification, menu)
        super.onCreateOptionsMenu(menu, inflater)
        notificationIcon = menu.getItem(0).icon
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_DASHBOARD -> {
                showPermissionErrorMessage(requireContext(), permissions, grantResults)
            }
        }
    }

    private fun setUp() {
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

        subscribeToLiveData()

        fab_qr.setOnClickListener {
            if (checkPermission(
                    listOf(
                        Manifest.permission.CAMERA
                    ), PERMISSION_DASHBOARD
                )
            ) {
                startActivity(ScanQRActivity.newIntent(requireContext()))
            }
        }

//        recycler_history_borrowed.apply {
//            adapter = HistoryBorrowedAdapter(
//                borrowingBookList.filter {
//                    it.isReturned == true
//                }.toMutableList()
//            ) {
//                startActivity(BookActivity.newIntent(context, it))
//            }
//
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//            addItemDecoration(MarginItemHorizontalDecoration(context.resources.getDimension(R.dimen.card_horizontal_margin)))
//        }

//        recycler_still_borrowing.apply {
//            adapter = StillBorrowingAdapter(
//                borrowingBookList.filter {
//                    it.isReturned == false
//                }.toMutableList()
//            ){
//                startActivity(BorrowingActivity.newIntent(context, it))
//            }
//            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
//            addItemDecoration(MarginItemHorizontalDecoration(context.resources.getDimension(R.dimen.card_horizontal_margin)))
//        }
    }

    private fun subscribeToLiveData() {
        mDashboardViewModel.imageUrlLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null && it.isNotEmpty()) {
                Glide.with(requireContext())
                    .load(it)
                    .placeholder(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.colorInactive)))
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(civ_image_header)
            } else {
                Glide.with(requireContext())
                    .load(ColorDrawable(ContextCompat.getColor(requireContext(), R.color.colorInactive)))
                    .into(civ_image_header)
            }
        })
    }

    companion object {
        const val PERMISSION_DASHBOARD = 1
    }
}
