package com.mdq.social.ui.addadmin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.BR
import com.mdq.social.R
import com.mdq.social.app.data.viewmodels.addadmin.AddAdminViewModel
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.base.BaseActivity
import com.mdq.social.databinding.ActivityAddadminBinding

class AddAdminActivity : BaseActivity<ActivityAddadminBinding, AddAdminNavigator>(), AddAdminNavigator {
    companion object {
        fun getCallingIntent(context: Context): Intent {
            return Intent(context, AddAdminActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
    private var activityAddadminBinding: ActivityAddadminBinding? = null
    private var addAdminViewModel: AddAdminViewModel? = null
    private var dialog: Dialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_addadmin
    }

    override fun getViewBindingVarible(): Int {
        return BR.addAdminViewModel
    }

    override fun getViewModel(): BaseViewModel<AddAdminNavigator> {
        addAdminViewModel =
            ViewModelProvider(this, viewModelFactory).get(AddAdminViewModel::class.java)
        return addAdminViewModel!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddadminBinding = getViewDataBinding()
        activityAddadminBinding?.addAdminViewModel = addAdminViewModel
        addAdminViewModel?.navigator = this
    }

    override fun updateVerify() {

    }


}