package com.example.download_manager.network

import com.example.download_manager.Constant
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url


/**
 * Created by gideon on 9/10/2022
 * gideon@cicil.co.id
 * https://www.cicil.co.id/
 */
interface ApiService {
    @Streaming
    @GET
    fun getPdfFile(
        @Url fileUrl: String,
        @Header("Authorization") authorization: String = Constant.TEXT_BLANK
    ): Call<ResponseBody>
}