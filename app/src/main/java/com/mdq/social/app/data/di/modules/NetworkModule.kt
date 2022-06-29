package com.mdq.social.app.data.di.modules

import com.mdq.social.app.data.api.ApiConstants
import com.mdq.social.app.data.api.ApiServiceGenerator
import com.mdq.social.app.data.api.AppApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mdq.social.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun providesGson(): Gson {
        return GsonBuilder().serializeNulls().setLenient().create()
    }

    @Provides
    @Singleton
    internal fun providesLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return logging
    }

    @Provides
    @Singleton
    internal fun provideApi(
        gson: Gson, loggingInterceptor: HttpLoggingInterceptor): AppApi {
        return ApiServiceGenerator.generate(
               ApiConstants.BASE_URL
            , AppApi::class.java, gson, loggingInterceptor)

    }
}
