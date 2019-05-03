package com.dicoding.naufal.mtoshokan.ui.ui.borrowing

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.model.BorrowingBook
import com.dicoding.naufal.mtoshokan.ui.ui.bookmarkbook.BookmarkBookActivity
import com.dicoding.naufal.mtoshokan.ui.utils.getRemaingDays
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_borrowing.*

class BorrowingActivity : AppCompatActivity() {

    var borrowingBook: BorrowingBook? = null

    var bookmarkIcon: Drawable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrowing)
        borrowingBook = intent.getParcelableExtra("borrowingBook")
        setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_bookmark -> {
                startActivity(borrowingBook?.book?.let { BookmarkBookActivity.newIntent(this, it) })
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

    fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (collapseToolbar.height + verticalOffset < 2 * ViewCompat.getMinimumHeight(collapseToolbar)) {
                ContextCompat.getColorStateList(this, R.color.colorPrimary)?.let { toolbar?.setTitleTextColor(it) }
                bookmarkIcon?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
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

        txt_title.text = borrowingBook?.book?.bookTitle
        txt_writer.text = borrowingBook?.book?.bookWriter
        txt_spec_title.text = borrowingBook?.book?.bookTitle
        txt_spec_writer.text = borrowingBook?.book?.bookWriter
        txt_spec_ISBN.text = borrowingBook?.book?.bookISBN

        txt_spec_borrowing_date.text = resources.getString(R.string.date_format, borrowingBook?.borrowingDate)

        txt_spec_return_date.text = resources.getString(R.string.date_format, borrowingBook?.returningDate)

        txt_spec_pinalty.text = borrowingBook?.returningDate?.getRemaingDays()?.let {
            if (it < 0) {
                txt_spec_pinalty.background = ContextCompat.getDrawable(this,
                    R.drawable.background_gradient_primary_rounded_unavailable
                )

                resources.getString(R.string.money_rp, 5000 * it * -1)

            } else {
                txt_spec_pinalty.background = ContextCompat.getDrawable(this,
                    R.drawable.background_gradient_primary_rounded_available
                )
                resources.getString(R.string.money_rp, 0)
            }
        }
    }

    companion object {
        fun newIntent(context: Context, borrowingBook: BorrowingBook): Intent {
            return Intent(context, BorrowingActivity::class.java).apply {
                putExtra("borrowingBook", borrowingBook)
            }
        }
    }
}
