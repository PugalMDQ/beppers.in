package com.mdq.social.base

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mdq.social.R
import com.mdq.social.app.data.bus.MainThreadBus
import com.mdq.social.app.data.preferences.AppPreference
import com.mdq.social.app.data.viewmodels.base.BaseViewModel
import com.mdq.social.databinding.ActivityBaseBinding
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseFragment<VB : ViewDataBinding, V : Any> : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appPreference: AppPreference
    @Inject
    lateinit var bus: MainThreadBus

    private var dialog: AlertDialog? = null
    var dialog1: Dialog? = null
    private var viewBinding: VB? = null
    private var viewModel: BaseViewModel<V>? = null
    private var baseBinding: ActivityBaseBinding?=null

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): BaseViewModel<V>

    abstract fun getViewBindingVarible(): Int

    fun getViewDataBinding(): VB {
        return viewBinding!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        baseBinding = DataBindingUtil.inflate(inflater, R.layout.activity_base, container, false)
        return baseBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataBinding()
        observeLoadingStatus()
    }

    private fun setDataBinding() {
        viewBinding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), baseBinding!!.frameContent, true)
        this.viewModel = getViewModel()
        viewBinding!!.setVariable(getViewBindingVarible(), viewModel)
        viewBinding!!.executePendingBindings()
    }

    protected fun showToast( input: String?) {
        Toast.makeText(context, input, Toast.LENGTH_SHORT).show()
    }

    protected fun showLoading() {
//        baseBinding!!.includProgress.progressBar.visibility = View.VISIBLE
    }

    public fun hideLoading() {
        baseBinding!!.includProgress.progressBar.visibility = View.INVISIBLE
    }

    protected fun observeLoadingStatus() {
        viewModel!!.loadingStatus.observe(viewLifecycleOwner, Observer { isLoading ->
            if (!isLoading!!) {
                hideLoading()
            } else {
                showLoading()
                Handler(Looper.getMainLooper()).postDelayed({
                  hideLoading()
                }, 2000)
            }
        })
    }

}