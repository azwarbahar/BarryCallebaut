package com.barrycallebaut.app.database.server

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val interceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(Interceptor { chain ->
            val originalRequest: Request = chain.request()
            val newRequest: Request = originalRequest.newBuilder()
//                .header("Content-Type", "application/json")
                .build()
            chain.proceed(newRequest)
        })
        addInterceptor(interceptor)
    }.build()

    val instances: ApiService by lazy {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://barrycallebaut.cfg.my.id/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(ApiService::class.java)
    }
}