package com.mdq.social.ui.Carousel

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class Adapter(val item:ArrayList<Fragment>, val Fm:FragmentManager) : FragmentStatePagerAdapter(Fm) {


    override fun getCount(): Int {
        return item.size
    }

    override fun getItem(position: Int): Fragment {
        return item[position]
    }

}