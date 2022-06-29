package com.mdq.social.ui.searchdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.getshopAlbumDetails.GetShopAlbumDetailsResponse
import com.mdq.social.app.data.response.getshopAlbumDetails.UserItem
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.searchdetails.SearchDetailsViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentPofileListBinding

class SearchProfileFragment:BaseFragment<FragmentPofileListBinding, SearchDetailsNavigator>(),
    ProfileSearchAdapter.ClickManager {
    companion object {
        fun getInstance(): Fragment {
            return SearchProfileFragment().apply {
            }
        }
    }

    private var fragmentPofileListBinding: FragmentPofileListBinding? = null
    private var searchDetailsViewModel: SearchDetailsViewModel? = null
    private var profileSearchAdapter: ProfileSearchAdapter? = null
    private var getShopAlbumDetailsResponse :GetShopAlbumDetailsResponse?=null

    override fun getLayoutId(): Int {
        return R.layout.fragment_pofile_list
    }

    override fun getViewModel(): BaseViewModel<SearchDetailsNavigator> {
        searchDetailsViewModel =
            ViewModelProvider(this, viewModelFactory).get(SearchDetailsViewModel::class.java)
        return searchDetailsViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.searchDetailsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentPofileListBinding = getViewDataBinding()

        if (arguments != null) {
            getShopAlbumDetailsResponse= arguments?.getSerializable(AppConstants.ALL) as GetShopAlbumDetailsResponse?
        }
    }

    override fun onItemClick(userItem: UserItem) {
        var bundle = Bundle()
        bundle.putInt(AppConstants.USER_ID, userItem.userId!!)
        NavHostFragment.findNavController(requireParentFragment())
            .navigate(R.id.navigation_user, bundle)

    }
}