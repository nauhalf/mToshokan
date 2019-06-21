package com.dicoding.naufal.mtoshokan.ui.book

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityBookBinding
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.ui.book.pageradapter.BookViewPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_book.*


class BookActivity : BaseActivity<ActivityBookBinding, SharedBookViewModel>() {
    private lateinit var bookViewPagerAdapter: BookViewPagerAdapter
    private lateinit var sharedBookViewModel: SharedBookViewModel
    private lateinit var binding: ActivityBookBinding
    override fun getLayoutId(): Int {
        return R.layout.activity_book
    }

    override fun getViewModel(): SharedBookViewModel {
        sharedBookViewModel = ViewModelProviders.of(this).get(SharedBookViewModel::class.java)
        return sharedBookViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.sharedBookViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        setUp()
    }

    fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (collapseToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapseToolbar)) {
                ContextCompat.getColorStateList(this, R.color.colorPrimary)?.let { toolbar?.setTitleTextColor(it) }
                toolbar.navigationIcon?.setColorFilter(
                    ContextCompat.getColor(this, R.color.colorPrimary),
                    PorterDuff.Mode.SRC_ATOP
                )
            } else {
                ContextCompat.getColorStateList(this, R.color.white)?.let { toolbar?.setTitleTextColor(it) }
                toolbar.navigationIcon?.setColorFilter(
                    ContextCompat.getColor(this, R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        })

        val bookId = intent.getStringExtra("bookId")
        bookViewPagerAdapter = BookViewPagerAdapter(supportFragmentManager, bookId)
        view_pager_book.adapter = bookViewPagerAdapter
        tab_layout_book.setupWithViewPager(view_pager_book)

        sharedBookViewModel.loadData(bookId)
        subscribeToLiveData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newIntent(ctx: Context, bookId: String): Intent {
            return Intent(ctx, BookActivity::class.java).apply {
                putExtra("bookId", bookId)
            }
        }
    }

    private fun subscribeToLiveData() {
        sharedBookViewModel.bookLiveData.observe(this, Observer {
            if (it != null) {

                txt_title.text = it.bookTitle
                txt_writer.text = it.bookWriter
                Glide.with(this)
                    .load(it.bookCover)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(img_cover)
            }
        })

        sharedBookViewModel.loading.observe(this, Observer {
            if(it){
                layout_loading.visibility = View.VISIBLE
            } else {
                layout_loading.visibility = View.GONE
            }
        })
    }
}
