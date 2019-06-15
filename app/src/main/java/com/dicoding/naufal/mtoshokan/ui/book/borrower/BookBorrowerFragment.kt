package com.dicoding.naufal.mtoshokan.ui.book.borrower


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.model.Book
import com.dicoding.naufal.mtoshokan.model.borrowingBookList
import com.dicoding.naufal.mtoshokan.ui.book.borrower.adapter.BookBorrowerAdapter
import kotlinx.android.synthetic.main.fragment_book_borrower.*


class BookBorrowerFragment : Fragment() {

    var book: Book? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_book_borrower, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        book = arguments?.getParcelable("book")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    fun setUp(){
        recycler_history_borrower.apply {
            adapter = BookBorrowerAdapter(
                borrowingBookList.filter {
                    it.book?.bookId == book?.bookId
                }.toMutableList()
            )

            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        }
    }

    companion object {
        fun newInstance(book: Book?) : BookBorrowerFragment = BookBorrowerFragment().apply{
            arguments = Bundle().apply {
                putParcelable("book", book)
            }
        }
    }
}
