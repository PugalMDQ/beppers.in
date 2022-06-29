package com.mdq.social.ui.searchdetails

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.app.AppConstants
import com.mdq.social.app.data.response.getshopAlbumDetails.NewPostsearchDeailResponse
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.searchdetails.SearchDetailsViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentVideoBinding

class VideoFrgment: BaseFragment<FragmentVideoBinding, SearchDetailsNavigator>() {

    companion object {
        fun getInstance(): Fragment {
            return VideoFrgment().apply {
            }
        }
    }
    private var fragmentVideoBinding: FragmentVideoBinding? = null
    private var searchDetailsViewModel: SearchDetailsViewModel? = null
    private var videoAdapter: VideoAdapter? = null
    private var layoutManager:RecyclerView.LayoutManager?=null
    val args=this.arguments
    private var getShopAlbumDetailsResponse : NewPostsearchDeailResponse?=null

    override fun getLayoutId(): Int {
        return R.layout.fragment_video
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
        fragmentVideoBinding = getViewDataBinding()

        Toast.makeText(requireContext(), "kdfnd"+getShopAlbumDetailsResponse?.data?.get(0)?.gallery, Toast.LENGTH_SHORT).show()
        if (args!= null) {
            Toast.makeText(requireContext(), "en6er"+getShopAlbumDetailsResponse?.data?.get(0)?.gallery, Toast.LENGTH_SHORT).show()
            getShopAlbumDetailsResponse= args.getSerializable(AppConstants.ALL) as NewPostsearchDeailResponse?
            Toast.makeText(requireContext(), ""+getShopAlbumDetailsResponse?.data?.get(0)?.gallery, Toast.LENGTH_SHORT).show()
        }

    }

}