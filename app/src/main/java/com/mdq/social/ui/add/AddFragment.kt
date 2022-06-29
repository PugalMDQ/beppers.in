package com.mdq.social.ui.add

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.viewmodels.add.AddViewModel
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.base.BaseFragment
import com.mdq.social.databinding.FragmentAddedBinding

class AddFragment: BaseFragment<FragmentAddedBinding, AddNavigator>(), AddNavigator {
    private var addViewModel: AddViewModel? = null
    private var fragmentAddedBinding: FragmentAddedBinding? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_added
    }

    override fun getViewModel(): BaseViewModel<AddNavigator> {
        addViewModel =
            ViewModelProvider(this, viewModelFactory).get(AddViewModel::class.java)
        return addViewModel!!
    }

    override fun getViewBindingVarible(): Int {
        return BR.addViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddedBinding = getViewDataBinding()
        fragmentAddedBinding?.addViewModel = addViewModel
        addViewModel?.navigator = this

    }
}