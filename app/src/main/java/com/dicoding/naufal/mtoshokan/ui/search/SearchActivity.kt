package com.dicoding.naufal.mtoshokan.ui.search

import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.itemdecoration.MarginItemHorizontalDecoration
import com.dicoding.naufal.mtoshokan.model.searchHistory
import com.dicoding.naufal.mtoshokan.ui.search.adapter.SearchHistoryAdapter
import com.dicoding.naufal.mtoshokan.ui.searchresult.SearchResultActivity
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*


class SearchActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val item = menu?.findItem(R.id.search)
        val searchView = item?.actionView as SearchView?
        searchView?.maxWidth = Int.MAX_VALUE
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView?.apply {
            setSearchableInfo(searchManager.getSearchableInfo(ComponentName(this@SearchActivity, SearchResultActivity::class.java)))
            setOnQueryTextListener(this@SearchActivity)
            requestFocus()
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
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

    private fun setUp() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recycler_history_search.apply {
            adapter = SearchHistoryAdapter(
                searchHistory.sortedByDescending {
                    it.time
                }.toMutableList()
            ){
                val intentSearch = Intent(Intent.ACTION_SEARCH)
                intentSearch.component = ComponentName(this@SearchActivity, SearchResultActivity::class.java)
                intentSearch.putExtra(SearchManager.QUERY, it.text)
                intentSearch.action = Intent.ACTION_SEARCH
                intentSearch.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_USER_ACTION
                startActivity(intentSearch)
            }

            layoutManager = ChipsLayoutManager.newBuilder(this@SearchActivity)

                .setScrollingEnabled(true)

                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build()
            addItemDecoration(MarginItemHorizontalDecoration(context.resources.getDimension(R.dimen.chip_horizontal_margin)))
        }
    }
}
