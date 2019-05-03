package com.dicoding.naufal.mtoshokan.ui.ui.bookmarkbook

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.model.Book
import com.dicoding.naufal.mtoshokan.ui.model.Bookmark
import com.dicoding.naufal.mtoshokan.ui.model.BookmarkBook
import com.dicoding.naufal.mtoshokan.ui.model.bookmarkList
import com.dicoding.naufal.mtoshokan.ui.ui.addbookmark.AddBookmarkActivity
import com.dicoding.naufal.mtoshokan.ui.ui.bookmarkbook.adapter.BookmarkBookAdapter
import kotlinx.android.synthetic.main.activity_bookmark_book.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class BookmarkBookActivity : AppCompatActivity() {

    var book: Book? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark_book)
        book = intent.getParcelableExtra("book")
        setUp()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            R.id.menu_add_bookmark -> {
                startActivityForResult(book?.let {
                    AddBookmarkActivity.newIntent(this,it)
                }, AddBookmarkActivity.REQ_CODE_ADD_BOOKMARK)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_bookmark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun setUp(){

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recycler_bookmark.apply {
            val data = book?.let {
                bookmarkList.filter {list->
                    list.book == it
                }.firstOrNull()?.bookmark?.toMutableList()
            } ?: run{
                mutableListOf<Bookmark>()
            }
            adapter = BookmarkBookAdapter(
                data
            ){
                TODO("not implemented")
            }

            layoutManager = LinearLayoutManager(this@BookmarkBookActivity, RecyclerView.VERTICAL, false)
        }

        txt_title.text = book?.bookTitle
        txt_writer.text  = book?.bookWriter
        txt_isbn.text = resources.getString(R.string.isbn_n, book?.bookISBN)
    }

    companion object {
        fun newIntent(context: Context, book: Book) : Intent {
            return Intent(context, BookmarkBookActivity::class.java).apply{
                putExtra("book", book)
            }
        }
    }
}
