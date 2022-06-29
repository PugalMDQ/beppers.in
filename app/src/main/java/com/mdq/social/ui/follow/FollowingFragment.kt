package com.mdq.social.ui.follow

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.followresponse.FollowingItem
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.following.FollowingViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentFollowingBinding
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment: BaseFragment < FragmentFollowingBinding, FollowingNavigator>(),
    FollowingNavigator, TextWatcher {
    companion object {
        fun getInstance(): Fragment {

            return FollowingFragment().apply {
            }
        }
    }

    private var followingViewModel: FollowingViewModel? = null
    private var fragmentFollowerBinding: FragmentFollowingBinding? = null
    private var followingAdapter: FollowingAdapter? = null
    private var followingItemResponse: List<FollowingItem>? = null
    private var filteredList = ArrayList<FollowingItem>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_following
    }

    override fun getViewModel(): BaseViewModel<FollowingNavigator> {
        followingViewModel =
            ViewModelProvider(this, viewModelFactory).get(FollowingViewModel::class.java)
        return followingViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.followingViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentFollowerBinding = getViewDataBinding()
        fragmentFollowerBinding?.followingViewModel = followingViewModel
        followingViewModel?.navigator = this

            editTextTextPersonName15.addTextChangedListener(this)

    }

    fun filter(query:String){

        var query=query.toString()
        filteredList.clear()

        for (items in followingItemResponse!!) {
            if (items.followingName?.contains(query)!!) {
                filteredList.add(items)
            }
        }

        followingAdapter?.setFollowingAdapter(filteredList)
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
        filter(s.toString())
    }

}