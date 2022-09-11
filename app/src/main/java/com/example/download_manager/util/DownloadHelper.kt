package com.example.download_manager.util

import android.content.Context
import com.example.download_manager.Constant
import com.example.download_manager.R
import okhttp3.ResponseBody
import timber.log.Timber
import java.io.*


/**
 * Created by gideon on 9/11/2022
 * gideon@cicil.co.id
 * https://www.cicil.co.id/
 */
object DownloadHelper {
    fun saveToDisk(
        body: ResponseBody,
        context: Context,
        onLoading: (Float) -> Unit,
        onSuccess: (File) -> Unit,
        onFailed: (Int) -> Unit
    ) {
        // state pdfSaved Path from Url by generate PdfId
        Timber.e("*** pdfSavedPathEXT  ${getFilePath(context)}")
        val destinationFile = File(getFilePath(context))
        try {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                Timber.e("*** try File Size=" + body.contentLength())
                inputStream = body.byteStream()
                outputStream = FileOutputStream(destinationFile)
                val data = ByteArray(4096)
                var count: Int
                var progress = 0
                while (inputStream.read(data).also { count = it } != -1) {
                    outputStream.write(data, 0, count)
                    progress += count
                    Timber.e(
                        "*** Progress: " + progress + "/" + body.contentLength() + " >>>> " + progress.toFloat() / body.contentLength()
                    )
                    Timber.e("name 4 ${Thread.currentThread().name}")
                    onLoading(progress.toFloat())
                }
                outputStream.flush()
                Timber.e("*** File saved successfully!")
                onSuccess(destinationFile)
                return
            } catch (e: IOException) {
                e.printStackTrace()
                Timber.e("*** Failed to save the file!")
                onFailed(R.string.failed_to_download)
                return
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            onFailed(R.string.failed_to_download)
            return
        }
    }

    private fun getFilePath(context: Context): String {
        return context.cacheDir.toString() +
                File.separator +
                Constant.PDF_NAME +
                Constant.PDF_EXTENSION
    }

    fun getFile(context: Context): File {
        return File(getFilePath(context))
    }
}

