package com.mdq.social.ui.search

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder
import com.mdq.social.BR
import com.mdq.social.PreferenceManager
import com.mdq.social.R
import com.mdq.social.app.data.response.addlikecomments.AddLikeCommentsResponse
import com.mdq.social.app.data.response.category.CategoryResponse
import com.mdq.social.app.data.response.category.DataItem
import com.mdq.social.app.data.response.recent.RecentResponse
import com.mdq.social.app.data.response.useralbum.RecommendedItem
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.search.SearchViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentSearchBinding
import com.mdq.social.ui.notification.NotificationActivity
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment: BaseFragment<FragmentSearchBinding, SearchNavigator>(),
    SearchNavigator,RecommendedAdapter.RecommendClickManager,TrendingAdapter.postClick {
    private var searchViewModel: SearchViewModel? = null
    private var fagmentSearchBinding: FragmentSearchBinding? = null
    private var exploreAdapter:ExploreAdapter?=null
    private var trendingAdapter:TrendingAdapter?=null
    private var recommendedAdapter:RecommendedAdapter?=null
    public var slidingRootNav: SlidingRootNav? = null
    private var preferenceManager:PreferenceManager?=null

    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun getViewModel(): BaseViewModel<SearchNavigator> {
        searchViewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchViewModel::class.java)
        return searchViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.searchViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fagmentSearchBinding = getViewDataBinding()
        fagmentSearchBinding?.searchViewModel = searchViewModel
        searchViewModel?.navigator = this

        ViewCompat.setNestedScrollingEnabled(rv_trend, false)

        slidingRootNav = SlidingRootNavBuilder(requireActivity())
            .withMenuOpened(false)
            .withContentClickableWhenMenuOpened(true)
            .withSavedState(savedInstanceState)
            .withMenuLayout(R.layout.menu_left_drawer)
            .inject()

        trendingAdapter = TrendingAdapter(requireContext(),this)
        rv_trend.adapter = trendingAdapter

        category()

        if(amIConnected()) {
            if(!appPreference.ADMINBLOCK.equals("1")) {
                getRecent(appPreference.USERID)
            }else{
                fagmentSearchBinding!!.BlockImage.visibility =View.VISIBLE
                fagmentSearchBinding!!.blockText.visibility =View.VISIBLE
            }
        }

        else{
            showToast("No Internet")
            rv_trend.visibility = View.VISIBLE
            in_tranding.visibility = View.GONE
            in_recommed.visibility = View.GONE
            trendinglistRecyclerViews(getPreferenceManager()?.getList()!!)
        }

    }

    private fun amIConnected(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun category() {
        searchViewModel!!.getCategory().observe(requireActivity(), Observer { response ->
            if (response?.data != null) {
                val categoryResponse = response.data as CategoryResponse
                if (categoryResponse != null && categoryResponse?.data != null) {
                    if (categoryResponse.data.size == 0) {
                        in_category.visibility = View.VISIBLE
                    } else {
                        in_category.visibility = View.GONE
                        categorylistRecyclerViews(categoryResponse.data as List<DataItem>)
                    }
                } else {
                    showToast(categoryResponse.message)
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    private fun getRecent(userId: String) {
        searchViewModel!!.getRecent(userId).observe(requireActivity(), Observer { response ->
            if (response?.data != null) {
                val recentResponse = response.data as RecentResponse
                if (recentResponse != null && recentResponse?.data != null) {
                    if (recentResponse.data.size == 0) {
                        rv_trend.visibility = View.GONE
                        in_tranding.visibility = View.VISIBLE
                        in_recommed.visibility = View.VISIBLE

                    } else {
                        rv_trend.visibility = View.VISIBLE
                        in_tranding.visibility = View.GONE
                        in_recommed.visibility = View.GONE

                        trendinglistRecyclerViews(recentResponse.data as List<com.mdq.social.app.data.response.recent.DataItem>)
                        trendingAdapter?.notifyDataSetChanged()
                    }
                } else {
                    showToast(recentResponse.message)
                }
            } else {
                showToast(response.throwable?.message!!)
            }
        })
    }

    private fun categorylistRecyclerViews(dataItem: List<DataItem>) {
        if (dataItem != null && dataItem.size > 0) {
            fagmentSearchBinding!!.executePendingBindings()
            exploreAdapter!!.setExploreAdapter(dataItem)
        }
    }

    private fun trendinglistRecyclerViews(trendingItem: List<com.mdq.social.app.data.response.recent.DataItem>) {
        val trendingItem1= ArrayList<com.mdq.social.app.data.response.recent.DataItem>()
        for(i in trendingItem.indices){
            if(!trendingItem.get(i).user_id!!.trim().equals(appPreference.USERID)){
                trendingItem1.add(trendingItem.get(i))
            }
        }
        if (trendingItem1 != null && trendingItem1.size > 0) {
            fagmentSearchBinding!!.executePendingBindings()
            trendingAdapter!!.setTrendingAdapter(trendingItem1)
        }
    }

    private fun recommendlistRecyclerViews(trendingItem: List<RecommendedItem>) {
        if (trendingItem != null && trendingItem.size > 0) {
            fagmentSearchBinding!!.executePendingBindings()
            recommendedAdapter!!.setRecommendedAdapter(trendingItem)
        }
    }

    override fun searchClick() {
        if(!appPreference.ADMINBLOCK.equals("1")) {
            NavHostFragment.findNavController(requireParentFragment())
                .navigate(R.id.navigation_search_details)
        }

    }

    override fun onItemLickClick(position: Int, imageView32: ImageView, textView96: TextView, recentItem: RecommendedItem) {
        searchViewModel!!.getAddLikeComments(
            recentItem.albumId.toString(),
            appPreference.USERID,
            "1",
            if (recentItem.like == 1) 0.toString() else 1.toString(),
            "",
            ""
        )
            .observe(requireActivity(), Observer { response ->
                if (response?.data != null) {
                    val addLikeCommentsResponse = response.data as AddLikeCommentsResponse
                    if (addLikeCommentsResponse != null) {
                        if (recentItem.like == 1) {

                            recentItem.totalLike =recentItem.totalLike?.minus(1)
                            imageView32.setImageResource(R.drawable.ic_empty_heart)
                            textView96.setText((recentItem.totalLike).toString())
                            recentItem.like=0

                        } else {

                            recentItem.totalLike =recentItem.totalLike?.plus(1)
                            imageView32.setImageResource(R.drawable.ic_heart)
                            textView96.setText((recentItem.totalLike).toString())
                            recentItem.like=1
                        }

                    } else {
                        showToast(addLikeCommentsResponse.message)
                    }
                } else {
                    showToast(response.throwable?.message!!)
                }
            })
    }
    @JvmName("getPreferenceManager1")
    fun getPreferenceManager(): PreferenceManager? {
        if (preferenceManager == null) {
            preferenceManager = PreferenceManager().getInstance()
            preferenceManager?.initialize(requireContext())
        }
        return preferenceManager
    }

    override fun onItemLickClicksOfProfile(
        id: String,
        gallery: String,
        user_id: String,
        position: Int
    ) {
        startActivity(
            Intent(requireContext(), NotificationActivity::class.java)
                .putExtra("PostAdapter", "PostAdapter")
                .putExtra("position",position)
                .putExtra("user_id",user_id)
        )
    }
}