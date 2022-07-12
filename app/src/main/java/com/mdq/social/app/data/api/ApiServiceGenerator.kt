package com.mdq.social.app.data.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiServiceGenerator {

    companion object {

        private const val API_TIMEOUT_MINUTES = 2

        fun <T> generate(
            baseUrl: String, serviceClass: Class<T>, gson: Gson, loggingInterceptor: HttpLoggingInterceptor
        ): T {

            val okHttpClientBuilder = OkHttpClient().newBuilder()
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
            okHttpClientBuilder.connectTimeout(API_TIMEOUT_MINUTES.toLong(), TimeUnit.MINUTES)
            okHttpClientBuilder.readTimeout(API_TIMEOUT_MINUTES.toLong(), TimeUnit.MINUTES)
            okHttpClientBuilder.writeTimeout(API_TIMEOUT_MINUTES.toLong(), TimeUnit.MINUTES)

            val okHttpClient = okHttpClientBuilder.build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()

            return retrofit.create(serviceClass)

        }
    }
}