package com.dicoding.naufal.mtoshokan.ui.book.information


import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dicoding.naufal.mtoshokan.BR
import com.dicoding.naufal.mtoshokan.R
import com.dicoding.naufal.mtoshokan.base.BaseFragment
import com.dicoding.naufal.mtoshokan.databinding.FragmentBookInformationBinding
import com.dicoding.naufal.mtoshokan.ui.book.SharedBookViewModel
import kotlinx.android.synthetic.main.fragment_book_information.*

class BookInformationFragment : BaseFragment<FragmentBookInformationBinding, SharedBookViewModel>() {

    lateinit var sharedBookViewModel: SharedBookViewModel
    lateinit var binding: FragmentBookInformationBinding

    override val bindingVariable: Int
        get() = BR.sharedBookViewModel

    override val layoutId: Int
        get() = R.layout.fragment_book_information

    override fun getViewModel(): SharedBookViewModel {
        this.sharedBookViewModel = activity?.let { ViewModelProviders.of(it).get(SharedBookViewModel::class.java) }!!
        return this.sharedBookViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = getViewDataBinding()
        setUp()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        sharedBookViewModel.bookLiveData.observe(viewLifecycleOwner, Observer {
            txt_spec_ISBN.text = it.bookISBN
            txt_spec_book_location.text = it.bookLocation
            txt_spec_book_type.text = it.bookType?.typeName
            txt_spec_writer.text = it.bookWriter
            txt_spec_title.text = it.bookTitle
            txt_spec_publisher.text = it.bookPublisher
            txt_synopsis.text = it.bookSynopsis

        })
        sharedBookViewModel.stockLiveData.observe(viewLifecycleOwner, Observer {
            if (it > 0) {
                txt_spec_book_availability.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_gradient_primary_rounded_available
                )
                txt_spec_book_availability.text = resources.getString(R.string.available)
            } else {
                txt_spec_book_availability.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.background_gradient_primary_rounded_unavailable
                )
                txt_spec_book_availability.text = resources.getString(R.string.unavailable)
            }
        })
    }

    fun setUp() {
//        txt_spec_book_availability.text = if (book?.bookAvaibility == true) {
//            txt_spec_book_availability.background = activity?.let {
//                ContextCompat.getDrawable(
//                    it,
//                    R.drawable.background_gradient_primary_rounded_available
//                )
//            }
//            resources.getString(R.string.available)
//        } else {
//            txt_spec_book_availability.background = activity?.let {
//                ContextCompat.getDrawable(
//                    it,
//                    R.drawable.background_gradient_primary_rounded_unavailable
//                )
//            }
//            resources.getString(R.string.unavailable)
//        }
    }

    companion object {
        fun newInstance(): BookInformationFragment = BookInformationFragment().apply {

        }
    }


}
