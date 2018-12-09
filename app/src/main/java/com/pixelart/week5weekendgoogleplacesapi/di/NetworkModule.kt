package com.pixelart.week5weekendgoogleplacesapi.di

import com.pixelart.week5weekendgoogleplacesapi.common.BASE_URL
import com.pixelart.week5weekendgoogleplacesapi.remote.APIService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun providesAPIService(retrofit: Retrofit):APIService{
        return retrofit.create(APIService::class.java)
    }

    @Provides
    fun providesRetrofit(okHttpClient: OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    fun providesOkHttpClient():OkHttpClient{
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

}