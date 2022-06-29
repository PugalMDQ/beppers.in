package com.mdq.social.ui.follow

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.response.followresponse.FollowerItem
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.follower.FollowerViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentFollowerBinding
import kotlinx.android.synthetic.main.fragment_follower.*


class FollowerFragment : BaseFragment<FragmentFollowerBinding, FollowerNavigator>(),
    FollowerNavigator, TextWatcher {

    companion object {
        fun getInstance(): Fragment {
            return FollowerFragment().apply {
            }
        }
    }

    private var followerViewModel: FollowerViewModel? = null
    private var fragmentFollowerBinding: FragmentFollowerBinding? = null
    private var followerAdapter: FollowerAdapter? = null
    private var followerItemResponse: List<FollowerItem>? = null
    private var filteredList = ArrayList<FollowerItem>()


    override fun getLayoutId(): Int {
        return R.layout.fragment_follower
    }

    override fun getViewModel(): BaseViewModel<FollowerNavigator> {
        followerViewModel =
            ViewModelProvider(this, viewModelFactory).get(FollowerViewModel::class.java)
        return followerViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.followerViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentFollowerBinding = getViewDataBinding()
        fragmentFollowerBinding?.followerViewModel = followerViewModel
        followerViewModel?.navigator = this


        editTextTextPersonName15.addTextChangedListener(this)

    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

        filter(s.toString())
    }


    public fun filter(query:String){

        var query=query.toString()
        filteredList.clear()

        for (items in followerItemResponse!!) {
            if (items.followerName?.contains(query)!!) {
                filteredList.add(items)
            }
        }

        followerAdapter?.setFollowerAdapter(filteredList)

    }
}