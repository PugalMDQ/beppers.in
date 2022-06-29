package com.mdq.social.ui.follow

import android.content.Context
import android.util.SparseArray
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.amilfreight.amilfreight.data.models.tab.TabsModel
import java.util.*

class TabAdapter(
    fm: FragmentManager,
    private val tabs: ArrayList<TabsModel>,
    private val context: Context) :
    FragmentStatePagerAdapter(fm) {
    private val registeredFragments = SparseArray<Fragment>()
    private var textColorResId: Int = 0
    private var iconColorResId: Int = 0

    fun setTextColor(textColorResId: Int) {
        this.textColorResId = textColorResId
    }

    fun setIconColor(iconColorResId: Int) {
        this.iconColorResId = iconColorResId
    }
    override fun getItem(position: Int): Fragment = when (position) {
        0 ->  FollowerFragment()
        1 -> FollowingFragment()
        else -> FollowerFragment()
    }

    override fun getCount(): Int {
        return tabs.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position].title
    }


    fun getTabString(position: Int): String? {

        val bean = tabs[position]
        return bean.title
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val fragment = super.instantiateItem(container, position) as Fragment

            registeredFragments.put(position, fragment)
        return fragment
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        registeredFragments.remove(position)
        super.destroyItem(container, position, `object`)
    }


}
