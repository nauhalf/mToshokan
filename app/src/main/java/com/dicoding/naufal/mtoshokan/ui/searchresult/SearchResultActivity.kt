package com.dicoding.naufal.mtoshokan.ui.searchresult

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.itemdecoration.MarginItemVerticalDecoration
import com.dicoding.naufal.mtoshokan.model.bookCreditRoll
import com.dicoding.naufal.mtoshokan.model.bookHyouka
import com.dicoding.naufal.mtoshokan.model.bookKudryavka
import com.dicoding.naufal.mtoshokan.model.bookOnajiYume
import com.dicoding.naufal.mtoshokan.ui.book.BookActivity
import com.dicoding.naufal.mtoshokan.ui.searchresult.adapter.SearchResultAdapter
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class SearchResultActivity : AppCompatActivity() {

    var querySearch: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)
        setUp()

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

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        newIntent()
        val query = SpannableStringBuilder()
            .append(resources.getString(R.string.search_with_keywords))
            .append(" ")
            .bold {
                append(querySearch)
            }

        txt_search_query.text = query

        recycler_result_search.apply {
            adapter = SearchResultAdapter(
                mutableListOf(
                    bookHyouka, bookCreditRoll, bookKudryavka, bookOnajiYume
                )
            ) {
                startActivity(BookActivity.newIntent(this@SearchResultActivity, it))
            }
            layoutManager = LinearLayoutManager(this@SearchResultActivity, RecyclerView.VERTICAL, false)

            addItemDecoration(MarginItemVerticalDecoration(resources.getDimension(R.dimen.card_vertical_margin)))
        }
    }

    private fun newIntent() {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                querySearch = query
            }
        }
    }
}
