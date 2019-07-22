package com.dicoding.naufal.mtoshokan.ui.bookmarkbook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivityBookmarkBookBinding
import com.dicoding.naufal.mtoshokan.ui.addbookmark.AddBookmarkActivity
import com.dicoding.naufal.mtoshokan.ui.bookmarkbook.adapter.BookmarkBookAdapter
import com.dicoding.naufal.mtoshokan.ui.detailbookmark.DetailBookmarkActivity
import kotlinx.android.synthetic.main.activity_bookmark_book.*
import kotlinx.android.synthetic.main.activity_bookmark_book.txt_isbn
import kotlinx.android.synthetic.main.activity_bookmark_book.txt_title
import kotlinx.android.synthetic.main.activity_bookmark_book.txt_writer
import kotlinx.android.synthetic.main.shimmer_activity_bookmark_book.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class BookmarkBookActivity : BaseActivity<ActivityBookmarkBookBinding, BookmarkBookViewModel>() {

    lateinit var bookId: String
    lateinit var mBookmarkBookAdapter: BookmarkBookAdapter
    lateinit var mBookmarkBookViewModel: BookmarkBookViewModel
    lateinit var binding: ActivityBookmarkBookBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_bookmark_book
    }

    override fun getViewModel(): BookmarkBookViewModel {
        mBookmarkBookViewModel = ViewModelProviders.of(this).get(BookmarkBookViewModel::class.java)
        return mBookmarkBookViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.bookmarkBookViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        bookId = intent.getStringExtra("bookId")
        setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_add_bookmark -> {
                startActivityForResult(mBookmarkBookViewModel.bookLiveData.value?.let {
                    AddBookmarkActivity.newIntent(this, it)
                }, AddBookmarkActivity.REQ_CODE_ADD_BOOKMARK)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == AddBookmarkActivity.REQ_CODE_ADD_BOOKMARK && resultCode == Activity.RESULT_OK){
            mBookmarkBookViewModel.getData(bookId)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_bookmark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun setUp() {

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mBookmarkBookAdapter = BookmarkBookAdapter(listener = {
            startActivity(DetailBookmarkActivity.newIntent(this, it))
        })

        recycler_bookmark.apply {
            adapter = mBookmarkBookAdapter
            layoutManager = LinearLayoutManager(this@BookmarkBookActivity, RecyclerView.VERTICAL, false)
        }

        mBookmarkBookViewModel.getData(bookId)
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        mBookmarkBookViewModel.bookLiveData.observe(this, Observer {
            if (it != null) {
                txt_title.text = it.bookTitle
                txt_writer.text = it.bookWriter
                txt_isbn.text = resources.getString(R.string.isbn_n, it.bookISBN)
                Glide.with(this)
                    .load(it.bookCover)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(img_cover)
            }
        })

        mBookmarkBookViewModel.bookmarkLiveData.observe(this, Observer {
            if (it != null) {
                mBookmarkBookAdapter.setData(it)
            }
        })

        mBookmarkBookViewModel.loading.observe(this, Observer {
            if(it){
                shimmer_activity_bookmark_book.startShimmer()
                shimmer_activity_bookmark_book.visibility = View.VISIBLE
            } else {
                shimmer_activity_bookmark_book.stopShimmer()
                shimmer_activity_bookmark_book.visibility = View.GONE
            }
        })
    }

    companion object {
        fun newIntent(context: Context, bookId: String): Intent {
            return Intent(context, BookmarkBookActivity::class.java).apply {
                putExtra("bookId", bookId)
            }
        }
    }
}
