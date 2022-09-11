package com.example.download_manager

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.download_manager.network.RetrofitBuilder
import com.example.download_manager.util.DownloadHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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


        viewModelScope.launch(Dispatchers.IO) {
            val service =
                RetrofitBuilder.service

            Timber.e("name 0 ${Thread.currentThread().name}")
            Timber.e("makeRequest")

            val response = service.getPdfFile(Constant.PDF_NAME + Constant.PDF_EXTENSION, "")
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    Timber.e("name 1 ${Thread.currentThread().name}")
                    DownloadHelper.saveToDisk(
                        response.body()!!, context,
                        onLoading = onLoading,
                        onSuccess = onSuccess,
                        onFailed = onFailed
                    )
                }
            } else {
                Timber.e("name 2 ${Thread.currentThread().name}")
                Timber.e("onFailure")
                withContext(Dispatchers.Main){
                    onFailed(R.string.failed_to_download)
                }
            }
        }
    }
}