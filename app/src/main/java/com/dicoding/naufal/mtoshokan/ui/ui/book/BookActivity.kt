package com.dicoding.naufal.mtoshokan.ui.ui.book

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.ImageViewCompat
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.model.Book
import com.dicoding.naufal.mtoshokan.ui.ui.book.pageradapter.BookViewPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_book.*


class BookActivity : AppCompatActivity() {
    var book: Book? = null
    lateinit var bookViewPagerAdapter: BookViewPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        book = intent.getParcelableExtra("book")
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

        txt_title.text = book?.bookTitle
        txt_writer.text = book?.bookWriter
        bookViewPagerAdapter = BookViewPagerAdapter(supportFragmentManager, book)
        view_pager_book.adapter = bookViewPagerAdapter
        tab_layout_book.setupWithViewPager(view_pager_book)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newIntent(ctx: Context, book: Book) : Intent {
            return Intent(ctx, BookActivity::class.java).apply {
                putExtra("book", book)
            }
        }
    }

}
