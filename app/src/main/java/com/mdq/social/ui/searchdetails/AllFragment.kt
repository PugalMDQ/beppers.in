package com.mdq.social.ui.searchdetails

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager.widget.ViewPager
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.getshopAlbumDetails.GetShopAlbumDetailsResponse
import com.mdq.social.app.data.response.getshopAlbumDetails.UserItem
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.searchdetails.SearchDetailsViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentAllBinding
import kotlinx.android.synthetic.main.fragment_all.*

class AllFragment : BaseFragment<FragmentAllBinding, SearchDetailsNavigator>(),
    SearchDetailsNavigator, ProfileSearchAdapter.ClickManager {
    companion object {
        fun getInstance(): Fragment {
            return AllFragment().apply {
            }
        }
    }
    private var fragmentAllBinding: FragmentAllBinding? = null
    private var searchDetailsViewModel: SearchDetailsViewModel? = null
    private var profileSearchAdapter: ProfileSearchAdapter? = null
    private var allVideoAdapter: AllVideoAdapter? = null
    private var allLocationAdapter: AllLocationAdapter? = null
    private var getShopAlbumDetailsResponse: GetShopAlbumDetailsResponse? = null
    private var photoAdapter: PhotoAdapter? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_all
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
        fragmentAllBinding = getViewDataBinding()

        ViewCompat.setNestedScrollingEnabled(rv_svideo, false)
        ViewCompat.setNestedScrollingEnabled(rv_slocation, false)
        ViewCompat.setNestedScrollingEnabled(rv_user, false)
        searchDetailsViewModel?.navigator = this


        if (arguments != null) {
            getShopAlbumDetailsResponse =
                arguments?.getSerializable(AppConstants.ALL) as GetShopAlbumDetailsResponse?
        }



        photoAdapter =
            PhotoAdapter(getShopAlbumDetailsResponse?.data?.get(0)?.image, requireActivity(),true)
        imageView31.adapter = photoAdapter

        allVideoAdapter =
            AllVideoAdapter(getShopAlbumDetailsResponse?.data?.get(0)?.vedios, requireActivity())
        rv_svideo.adapter = allVideoAdapter

        allLocationAdapter = AllLocationAdapter(
            getShopAlbumDetailsResponse?.data?.get(0)?.location,
            requireActivity()
        )
        rv_slocation.adapter = allLocationAdapter


    }

    override fun viewAllClick(viewall: Int) {
        var vpSearch=requireActivity().findViewById<ViewPager>(R.id.vp_search)

        when (viewall) {
            1 -> {

                vpSearch.setCurrentItem(1)
            }
            2 -> {
                vpSearch.setCurrentItem(2)

            }
            3 -> {
                vpSearch.setCurrentItem(3)

            }
            4 -> {
                vpSearch.setCurrentItem(4)

            }

        }
    }

    override fun onItemClick(userItem: UserItem) {
        var bundle = Bundle()
        bundle.putInt(AppConstants.USER_ID, userItem.userId!!)
        NavHostFragment.findNavController(requireParentFragment())
            .navigate(R.id.navigation_user, bundle)    }
}