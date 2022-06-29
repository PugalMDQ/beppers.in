package com.mdq.social.ui.searchdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.getshopAlbumDetails.GetShopAlbumDetailsResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.searchdetails.SearchDetailsViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentLocationBinding

class LocationFragment: BaseFragment<FragmentLocationBinding, SearchDetailsNavigator>() {
    companion object {
        fun getInstance(): Fragment {
            return LocationFragment().apply {
            }
        }
    }


    private var fragmentLocationBinding: FragmentLocationBinding? = null
    private var searchDetailsViewModel: SearchDetailsViewModel? = null
    private var locationAdapter: LocationAdapter? = null

    private var getShopAlbumDetailsResponse :GetShopAlbumDetailsResponse?=null


    override fun getLayoutId(): Int {
        return R.layout.fragment_location
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
        fragmentLocationBinding = getViewDataBinding()

        if (arguments != null) {
            getShopAlbumDetailsResponse= arguments?.getSerializable(AppConstants.ALL) as GetShopAlbumDetailsResponse?
        }

        locationAdapter = LocationAdapter(getShopAlbumDetailsResponse?.data?.get(0)?.location,requireActivity())

        fragmentLocationBinding?.locationAdapter=locationAdapter
    }

}