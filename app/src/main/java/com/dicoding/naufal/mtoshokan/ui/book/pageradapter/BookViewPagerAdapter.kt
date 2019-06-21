package com.dicoding.naufal.mtoshokan.ui.book.pageradapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dicoding.naufal.mtoshokan.ui.book.borrower.BookBorrowerFragment
import com.dicoding.naufal.mtoshokan.ui.book.information.BookInformationFragment

class BookViewPagerAdapter(private val fragmentManager: FragmentManager, val bookId: String?) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> BookInformationFragment.newInstance()
            else -> BookBorrowerFragment.newInstance(bookId)
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Informasi"
            else -> "Riwayat Peminjam"
        }
    }
}