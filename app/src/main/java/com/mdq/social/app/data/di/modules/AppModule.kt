package com.mdq.social.app.data.di.modules

import android.content.Context
import com.mdq.social.app.data.app.AppController
import com.mdq.social.app.data.bus.MainThreadBus
import com.mdq.social.app.data.preferences.AppPreference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun providesContext(): Context {
        return AppController.getInstance()!!.applicationContext
    }

    @Provides
    @Singleton
    internal fun providesAppPreference(context: Context): AppPreference {
        return AppPreference(context)
    }

    @Provides
    @Singleton
    internal fun providesMainThreadBus(): MainThreadBus {
        return MainThreadBus()
    }




}