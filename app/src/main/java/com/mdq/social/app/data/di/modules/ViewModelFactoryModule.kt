package com.mdq.social.app.data.di.modules

import androidx.lifecycle.ViewModelProvider
import com.mdq.social.app.data.factory.DaggerViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factoryApp: DaggerViewModelFactory): ViewModelProvider.Factory

}