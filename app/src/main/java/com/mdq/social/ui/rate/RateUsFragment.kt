package com.mdq.social.ui.rate

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.rate.RateUsViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentRateusBinding


class RateUsFragment: BaseFragment<FragmentRateusBinding, RateUsNavigator>(),
    RateUsNavigator {
    private var rateUsViewModel: RateUsViewModel? = null
    private var fragmentRateusBinding: FragmentRateusBinding? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_rateus
    }

    override fun getViewModel(): BaseViewModel<RateUsNavigator> {
        rateUsViewModel =
            ViewModelProvider(this, viewModelFactory).get(RateUsViewModel::class.java)
        return rateUsViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.rateUsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentRateusBinding = getViewDataBinding()
        fragmentRateusBinding?.rateUsViewModel = rateUsViewModel
        rateUsViewModel?.navigator = this
    }
}