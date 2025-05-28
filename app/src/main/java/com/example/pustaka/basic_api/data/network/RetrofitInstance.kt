package com.example.pustaka.basic_api.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL_JSONPLACEHOLDER = "https://jsonplaceholder.typicode.com"
    private const val BASE_URL_CRUDAPI = "https://crudapi.co.uk/api/v1/"

    private const val BASE_URL_PERPUS_API = "http://103.217.145.203:3000/api/"

    val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    fun getJsonPlaceHolderApi(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_JSONPLACEHOLDER)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun getCrudApi(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_CRUDAPI)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    fun getPerpusApi(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_PERPUS_API)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}