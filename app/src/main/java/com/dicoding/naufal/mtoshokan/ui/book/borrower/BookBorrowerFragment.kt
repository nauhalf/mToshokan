package com.dicoding.naufal.mtoshokan.ui.book.borrower


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseFragment
import com.dicoding.naufal.mtoshokan.databinding.FragmentBookBorrowerBinding
import com.dicoding.naufal.mtoshokan.ui.book.borrower.adapter.BookBorrowerAdapter
import kotlinx.android.synthetic.main.fragment_book_borrower.*


class BookBorrowerFragment : BaseFragment<FragmentBookBorrowerBinding, BookBorrowerViewModel>() {

    private var bookId: String? = null
    lateinit var mBookBorrowerViewModel: BookBorrowerViewModel
    lateinit var binding: FragmentBookBorrowerBinding
    lateinit var mAdapter: BookBorrowerAdapter

    override val bindingVariable: Int
        get() = BR.bookBorrowerViewModel
    override val layoutId: Int
        get() = R.layout.fragment_book_borrower

    override fun getViewModel(): BookBorrowerViewModel {
        mBookBorrowerViewModel = ViewModelProviders.of(this).get(BookBorrowerViewModel::class.java)
        return mBookBorrowerViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookId = arguments?.getString("bookId")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        setUp()

    }

    private fun setUp() {
        mAdapter = BookBorrowerAdapter()

        recycler_history_borrower.apply {
            adapter = mAdapter

            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }
        mBookBorrowerViewModel.bookId.value = bookId
        mBookBorrowerViewModel.loadData()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData(){
        mBookBorrowerViewModel.borrowerLiveData.observe(viewLifecycleOwner  , Observer {
            mAdapter.addList(it)
        })

        mBookBorrowerViewModel.loading.observe(viewLifecycleOwner, Observer {
            layout_loading.visibility = if(it){
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }

    companion object {
        fun newInstance(bookId: String?): BookBorrowerFragment = BookBorrowerFragment().apply {
            arguments = Bundle().apply {
                putString("bookId", bookId)
            }
        }
    }
}
