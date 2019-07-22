package com.dicoding.naufal.mtoshokan.ui.main.bookmark


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.itemdecoration.MarginItemGridDecoration
import kotlinx.android.synthetic.main.fragment_bookmark.*
import kotlinx.android.synthetic.main.template_toolbar_gradient_rtl.*

class BookmarkFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmark, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    fun setUp() {
        val activity = (activity as AppCompatActivity)
        activity.setSupportActionBar(toolbar)
        activity.supportActionBar?.title = "Penanda"
        recycler_bookmarked_book.apply {
            //            adapter = BookmarkAdapter( listener = {
//                startActivity(BookmarkBookActivity.newIntent(context, it))
//            }

            layoutManager = GridLayoutManager(context, 3)
            addItemDecoration(MarginItemGridDecoration(resources.getDimension(R.dimen.card_horizontal_margin), 3))
        }
    }
}
