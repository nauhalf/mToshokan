package com.dicoding.naufal.mtoshokan.ui.borrowing

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityBorrowingBinding
import com.dicoding.naufal.mtoshokan.model.BorrowingBook
import com.dicoding.naufal.mtoshokan.ui.bookmarkbook.BookmarkBookActivity
import com.dicoding.naufal.mtoshokan.utils.getRemaingDays
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_borrowing.*

class BorrowingActivity : BaseActivity<ActivityBorrowingBinding, BorrowingViewModel>() {

    private var borrowingBook: BorrowingBook? = null
    private lateinit var mBorrowingViewModel: BorrowingViewModel
    private var bookmarkIcon: Drawable? = null
    private lateinit var binding: ActivityBorrowingBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_borrowing
    }

    override fun getViewModel(): BorrowingViewModel {
        mBorrowingViewModel = ViewModelProviders.of(this).get(BorrowingViewModel::class.java)
        return mBorrowingViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.borrowingViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_bookmark -> {
                startActivity(mBorrowingViewModel.borrowingLiveData.value?.book?.let {
                    it.bookId?.let { it1 ->
                        BookmarkBookActivity.newIntent(
                            this,
                            it1
                        )
                    }
                })
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_bookmark, menu)
        bookmarkIcon = menu?.getItem(0)?.icon
        return true
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (collapseToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapseToolbar)) {
                ContextCompat.getColorStateList(this, R.color.colorPrimary)?.let { toolbar?.setTitleTextColor(it) }
                bookmarkIcon?.setColorFilter(
                    ContextCompat.getColor(this, R.color.colorPrimary),
                    PorterDuff.Mode.SRC_ATOP
                )
                toolbar.navigationIcon?.setColorFilter(
                    ContextCompat.getColor(this, R.color.colorPrimary),
                    PorterDuff.Mode.SRC_ATOP
                )
            } else {
                ContextCompat.getColorStateList(this, R.color.white)?.let { toolbar?.setTitleTextColor(it) }
                bookmarkIcon?.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
                toolbar.navigationIcon?.setColorFilter(
                    ContextCompat.getColor(this, R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        })

        mBorrowingViewModel.borrowingLiveData.value = intent.getParcelableExtra("borrowingBook")
        subscribeToLiveData()


    }

    private fun subscribeToLiveData() {
        mBorrowingViewModel.borrowingLiveData.observe(this, Observer {
            if (it != null) {
                txt_title.text = it.book?.bookTitle

                txt_writer.text = it.book?.bookWriter
                txt_spec_title.text = it.book?.bookTitle
                txt_spec_writer.text = it.book?.bookWriter
                txt_spec_ISBN.text = it.book?.bookISBN

                txt_spec_borrowing_date.text = resources.getString(R.string.date_format, it.borrowingDate)

                txt_spec_return_date.text = resources.getString(R.string.date_format, it.returningDate)

                Glide.with(this)
                    .load(it.book?.bookCover)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(img_cover)

                if (it.isReturned == true) {
                    txt_spec_status.text = getString(R.string.return_yes)
                    if (it.pinaltyAmount.isNullOrEmpty()) {
                        txt_spec_pinalty.background = ContextCompat.getDrawable(
                            this,
                            R.drawable.background_gradient_primary_rounded_available
                        )

                        txt_spec_pinalty.text = resources.getString(R.string.money_rp, 0)
                    } else {
                        txt_spec_pinalty.background = ContextCompat.getDrawable(
                            this,
                            R.drawable.background_gradient_primary_rounded_available
                        )
                        txt_spec_pinalty.text =
                            resources.getString(R.string.money_rp, it.pinaltyAmount?.toInt())
                    }
                } else {

                    txt_spec_status.text = getString(R.string.return_no)
                    it.returningDate?.getRemaingDays()?.let { remaining ->
                        if (remaining < 0) {
                            txt_spec_pinalty.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.background_gradient_primary_rounded_unavailable
                            )

                            txt_spec_pinalty.text = resources.getString(
                                R.string.money_rp,
                                (5000 * remaining * -1)
                            )
                        } else {
                            txt_spec_pinalty.background = ContextCompat.getDrawable(
                                this,
                                R.drawable.background_gradient_primary_rounded_available
                            )
                            txt_spec_pinalty.text = resources.getString(R.string.money_rp, 0)
                        }
                    }
                }
            }
        })
    }

    companion object {
        fun newIntent(context: Context, borrowingBook: BorrowingBook): Intent {
            return Intent(context, BorrowingActivity::class.java).apply {
                putExtra("borrowingBook", borrowingBook)
            }
        }
    }
}
