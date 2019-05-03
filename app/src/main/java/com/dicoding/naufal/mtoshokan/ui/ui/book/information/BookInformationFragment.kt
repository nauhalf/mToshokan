package com.dicoding.naufal.mtoshokan.ui.ui.book.information


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.ui.model.Book
import kotlinx.android.synthetic.main.fragment_book_information.*

class BookInformationFragment : Fragment() {

    var book: Book? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_information, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        book = bundle?.getParcelable("book")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    fun setUp() {
        txt_synopsis.text = book?.bookSynopsis
        txt_spec_title.text = book?.bookTitle
        txt_spec_writer.text = book?.bookWriter
        txt_spec_ISBN.text = book?.bookISBN
        txt_spec_publisher.text = book?.bookPublisher
        txt_spec_book_type.text = book?.bookType?.typeName
        txt_spec_book_location.text = book?.bookLocation
        txt_spec_book_availability.text = if (book?.bookAvaibility == true) {
            txt_spec_book_availability.background = activity?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.background_gradient_primary_rounded_available
                )
            }
            resources.getString(R.string.available)
        } else {
            txt_spec_book_availability.background = activity?.let {
                ContextCompat.getDrawable(
                    it,
                    R.drawable.background_gradient_primary_rounded_unavailable
                )
            }
            resources.getString(R.string.unavailable)
        }
    }

    companion object {
        fun newInstance(book: Book?) : BookInformationFragment = BookInformationFragment().apply {
            arguments = Bundle().apply {
                putParcelable("book", book)
            }
        }
    }


}
