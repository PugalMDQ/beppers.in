package com.mdq.social.ui.terms

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.app.data.viewmodels.terms.TermsViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentTermsBinding

class TermsFragment : BaseFragment<FragmentTermsBinding, TermsNavigator>(),
    TermsNavigator {
    private var termsViewModel: TermsViewModel? = null
    private var fragmentTermsBinding: FragmentTermsBinding? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_terms
    }

    override fun getViewModel(): BaseViewModel<TermsNavigator> {
        termsViewModel =
            ViewModelProvider(this, viewModelFactory).get(TermsViewModel::class.java)
        return termsViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.termsViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentTermsBinding = getViewDataBinding()
        fragmentTermsBinding?.termsViewModel = termsViewModel
        termsViewModel?.navigator = this
    }

}