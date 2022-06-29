package com.mdq.social.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
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
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


abstract class BaseActivity<VB : ViewDataBinding, V : Any>: DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    open lateinit var appPreference: AppPreference
    @Inject
    lateinit var bus: MainThreadBus

    private var dialog: AlertDialog? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base)
        setDataBinding()
        observeLoadingStatus()
    }

    private fun setDataBinding() {
        viewBinding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), baseBinding!!.frameContent, true)
        this.viewModel = getViewModel()
        viewBinding!!.setVariable(getViewBindingVarible(), viewModel)
        viewBinding!!.executePendingBindings()
    }

    fun setHeaderTitle(title: String) {
        if (supportActionBar != null) supportActionBar!!.title = title
    }

    fun showBackButton(status: Boolean) {
        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(status)
        if (supportActionBar != null)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.white_back)
    }

    fun showToast(input: String) {
        Toast.makeText(this, input, Toast.LENGTH_LONG).show()
    }

    fun showLoading() {
        baseBinding!!.includProgress.progressBar.visibility = View.VISIBLE
    }

    fun hideLoading() {
        baseBinding!!.includProgress.progressBar.visibility = View.INVISIBLE
    }

//    fun observeLoadingStatus() {
//        viewModel!!.loadingStatus.observe(this, Observer { isLoading ->
//            if (!isLoading!!) {
//                hideLoading()
//            } else {
//                showLoading()
//            }
//        })
//    }

    fun observeLoadingStatus() {
        viewModel!!.loadingStatus.observe(this, Observer { isLoading ->
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!!.itemId == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)

    }


}