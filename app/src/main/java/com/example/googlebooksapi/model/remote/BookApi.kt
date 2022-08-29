package com.example.googlebooksapi.model.remote

import com.example.googlebooksapi.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class BookApi {
    val api : BookService by lazy {
        initRetrofit()
    }

    private fun initRetrofit(): BookService{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(getOkHttpClient())
            .build()
            .create(BookService::class.java)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val okhttpLogging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG)
                okhttpLogging.level = HttpLoggingInterceptor.Level.BASIC
            else
                okhttpLogging.level = HttpLoggingInterceptor.Level.NONE

        val client = OkHttpClient.Builder()
            .addInterceptor(okhttpLogging)
            .build()
        return client
    }
}