package com.mdq.social.ui.pendingrequest

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mdq.social.ui.follow.FollowerFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment = when (position) {
        0 ->  RequestFragment()
        1 -> IgnoredFragment()
        else -> FollowerFragment()
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val names = arrayOf<CharSequence>("Requests", "Ignored")
        return names[position]
    }

}