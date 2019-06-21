package com.dicoding.naufal.mtoshokan.ui.searchresult

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseActivity
import com.dicoding.naufal.mtoshokan.databinding.ActivitySearchResultBinding
import com.dicoding.naufal.mtoshokan.itemdecoration.MarginItemVerticalDecoration
import com.dicoding.naufal.mtoshokan.ui.book.BookActivity
import com.dicoding.naufal.mtoshokan.ui.searchresult.adapter.SearchResultAdapter
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.shimmer_activity_search_result.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class SearchResultActivity : BaseActivity<ActivitySearchResultBinding, SearchResultViewModel>() {

    private lateinit var querySearch: String
    lateinit var searchResultAdapter: SearchResultAdapter
    lateinit var searchResultViewModel: SearchResultViewModel
    lateinit var binding: ActivitySearchResultBinding

    override fun getLayoutId(): Int {
        return R.layout.activity_search_result
    }

    override fun getViewModel(): SearchResultViewModel {
        searchResultViewModel = ViewModelProviders.of(this).get(SearchResultViewModel::class.java)
        return searchResultViewModel
    }

    override fun getBindingVariable(): Int {
        return BR.searchResultViewModel
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        searchResultAdapter = SearchResultAdapter {
            startActivity(it.bookId?.let { it1 -> BookActivity.newIntent(this@SearchResultActivity, it1) })
        }

        newIntent()

        recycler_result_search.apply {
            adapter = searchResultAdapter
            layoutManager = LinearLayoutManager(this@SearchResultActivity, RecyclerView.VERTICAL, false)

            addItemDecoration(MarginItemVerticalDecoration(resources.getDimension(R.dimen.card_vertical_margin)))
        }

        subscribeToLiveData()

        searchResultViewModel.loadSearchData(querySearch)
    }

    private fun newIntent() {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                querySearch = query
            }
        }
    }

    private fun subscribeToLiveData() {
        searchResultViewModel.querySearch.observe(this, Observer {
            if (it.isNotEmpty()) {
                searchResultAdapter.setQuery(it)
            }
        })

        searchResultViewModel.bookList.observe(this, Observer {
            Log.d("Data => ", it.toString())
            searchResultAdapter.setData(it)
        })

        searchResultViewModel.loading.observe(this, Observer {
            if (it) {
                shimmer_activity_search_result.startShimmer()
                shimmer_activity_search_result.visibility = View.VISIBLE
            } else {
                shimmer_activity_search_result.stopShimmer()
                shimmer_activity_search_result.visibility = View.GONE
            }
        })
    }


    override fun onResume() {
        if (searchResultViewModel.loading.value!!) shimmer_activity_search_result.startShimmer()
        super.onResume()
    }

    override fun onPause() {
        if (searchResultViewModel.loading.value!!) shimmer_activity_search_result.stopShimmer()
        super.onPause()
    }
}
