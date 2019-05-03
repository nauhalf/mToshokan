package com.dicoding.naufal.mtoshokan.ui.ui.addbookmark

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.itemdecoration.MarginItemGridDecoration
import com.dicoding.naufal.mtoshokan.ui.model.Book
import com.dicoding.naufal.mtoshokan.ui.ui.addbookmark.adapter.AddPhotoBookmarkAdapter
import kotlinx.android.synthetic.main.activity_add_bookmark.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class AddBookmarkActivity : AppCompatActivity() {

    var book: Book? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_bookmark)
        book = intent.getParcelableExtra("book")
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_save, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun setUp(){
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        recycler_bookmark_photo.apply {
            adapter = AddPhotoBookmarkAdapter(
                mutableListOf()
            )
            layoutManager = GridLayoutManager(this@AddBookmarkActivity, 3)
            addItemDecoration(MarginItemGridDecoration(resources.getDimension(R.dimen.card_horizontal_margin),3))
        }

        txt_title.text = book?.bookTitle
        txt_writer.text = book?.bookWriter
        txt_isbn.text = resources.getString(R.string.isbn_n, book?.bookISBN)
    }

    companion object {

        const val REQ_CODE_ADD_BOOKMARK = 1
        fun newIntent(context: Context, book: Book) : Intent {
            return Intent(context, AddBookmarkActivity::class.java).apply {
                putExtra("book", book)
            }
        }
    }
}
