package com.dicoding.naufal.mtoshokan.ui.ui.book.pageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.fragment.app.ListFragment
import com.dicoding.naufal.mtoshokan.ui.model.Book
import com.dicoding.naufal.mtoshokan.ui.ui.book.borrower.BookBorrowerFragment
import com.dicoding.naufal.mtoshokan.ui.ui.book.information.BookInformationFragment

class BookViewPagerAdapter(private val fragmentManager: FragmentManager, val book: Book?) : FragmentStatePagerAdapter(fragmentManager){

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> BookInformationFragment.newInstance(book)
            else -> BookBorrowerFragment.newInstance(book)
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Informasi"
            else -> "Riwayat Peminjam"
        }
    }
}