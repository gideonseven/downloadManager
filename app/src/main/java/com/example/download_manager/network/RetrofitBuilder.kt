package com.example.download_manager.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit


/**
 * Created by gideon on 9/10/2022
 * gideon@cicil.co.id
 * https://www.cicil.co.id/
 */
object RetrofitBuilder {
    private const val BASE_URL = "https://unec.edu.az/application/uploads/2014/12/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(OkHttpClient())
        .build()

    var service: ApiService = retrofit.create(ApiService::class.java)
}