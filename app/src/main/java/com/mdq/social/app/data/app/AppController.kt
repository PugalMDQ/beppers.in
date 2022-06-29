package com.mdq.social.app.data.app

import com.mdq.social.app.data.bus.MainThreadBus
import com.mdq.social.app.data.di.componants.DaggerAppComponant
import com.mdq.social.app.data.preferences.AppPreference
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class AppController : DaggerApplication() {
    private var bus: MainThreadBus? = null
    private var appPreference: AppPreference?=null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponant.builder().create(this)
    }

    companion object {
        private var appController: AppController? = null

        @Synchronized
        fun getInstance(): AppController? {
            return appController
        }
    }


    override fun onCreate() {
        super.onCreate()
        appController = this
        bus = MainThreadBus()
        appPreference = AppPreference(this)

    }

    fun getBus(): MainThreadBus {
        return bus!!
    }

    fun getAppPreference(): AppPreference? {
        return appPreference!!
    }
}
