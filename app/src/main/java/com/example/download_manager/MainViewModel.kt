package com.example.download_manager

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.download_manager.network.RetrofitBuilder
import com.example.download_manager.util.DownloadHelper
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File


/**
 * Created by gideon on 9/11/2022
 * gideon@cicil.co.id
 * https://www.cicil.co.id/
 */
class MainViewModel : ViewModel() {

    fun makeRequest(
        context: Context,
        onLoading: (Float) -> Unit,
        onSuccess: (File) -> Unit,
        onFailed: (Int) -> Unit
    ) {
        viewModelScope.launch {
            Timber.e("makeRequest")
            val repos =
                RetrofitBuilder.service.getPdfFile(Constant.PDF_NAME + Constant.PDF_EXTENSION, "")
            repos.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    Timber.e("onResponse ${response.raw()}")
                    Timber.e("onResponse ${response.errorBody().toString()}")

                    if (response.isSuccessful) {
                        DownloadHelper.saveToDisk(
                            response.body()!!, context,
                            onLoading = onLoading,
                            onSuccess = onSuccess,
                            onFailed = onFailed
                        )
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.e("onFailure")
                    Timber.e("${t.message}")
                    onFailed(R.string.failed_to_download)
                }
            })
        }
    }
}