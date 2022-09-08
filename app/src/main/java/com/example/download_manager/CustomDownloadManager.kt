package com.example.download_manager

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.DownloadManager.*
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File


/**
 * Created by gideon on 9/8/2022
 * gideon@cicil.co.id
 * https://www.cicil.co.id/
 */
class CustomDownloadManager {
    companion object {
        const val EXAMPLE_FOLDER = "example"
        const val PDF_EXTENSION = ".pdf"
        const val PDF_NAME = "sample"
        const val AUTH_HEADER = "Authorization"
        const val TEXT_BLANK = ""
    }

    @SuppressLint("Range")
    fun downloadPdf(
        context: Context,
        lifecycleScope: LifecycleCoroutineScope,
        activeToken: String = TEXT_BLANK,
        pdfUrl: String?,
        onDownloadFailed: (errorMessage: String) -> Unit = {},
        onDownloadPauseOrRunning: () -> Unit = {},
        onDownloadPending: () -> Unit = {},
        onDownloadSuccess: (pdSavedPath: String) -> Unit = {}
    ) {

        //set DownloadManager id to avoid declaring multiple download
        val myDownloadId: Long

        // state pdfSaved Path from Url by generate PdfId
        val pdfSavedPath = context.getExternalFilesDir(EXAMPLE_FOLDER)
            .toString() + File.separator + PDF_NAME + PDF_EXTENSION
        Timber.e("=== pdfSavedPathEXT  $pdfSavedPath")

        // avoid duplicated file downloaded again
        // if no file exist, start downloading
        if (!File(pdfSavedPath).exists()) {
            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

            Timber.e("=== pdfUrl ${pdfUrl?.trim()}")
            if (pdfUrl.isNullOrEmpty()) return
            val request = Request(Uri.parse(pdfUrl.trim())).apply {
                // Title and Description is hidden, no need to Setup
                // setTitle("Downloading PDF")
                // setDescription("Now downloading")
                setAllowedNetworkTypes(Request.NETWORK_WIFI or Request.NETWORK_MOBILE)
                setAllowedOverRoaming(false)
                // If Required Authorization Header, we could Add It here
                if (activeToken.isNotBlank() && activeToken.isNotEmpty()) {
                    addRequestHeader(AUTH_HEADER, activeToken)
                }
                //set to notify when completed
                //setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setNotificationVisibility(Request.VISIBILITY_HIDDEN)
                setDestinationInExternalFilesDir(
                    context,
                    EXAMPLE_FOLDER,
                    PDF_NAME + PDF_EXTENSION
                )
            }
            myDownloadId = downloadManager.enqueue(request)
            val query = Query().setFilterById(myDownloadId)

            lifecycleScope.launchWhenStarted {
                Timber.e("=== launchWhenStarted")

                var downloading = true
                while (downloading) {
                    val cursor: Cursor = downloadManager.query(query)
                    cursor.moveToFirst()
                    if (cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)) == STATUS_SUCCESSFUL) {
                        downloading = false
                    }
                    val status = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS))
                    when (status) {
                        STATUS_PENDING -> {
                            Timber.e("=== STATUS_PENDING")
                            onDownloadPending()
                        }
                        STATUS_PAUSED, STATUS_RUNNING -> {
                            Timber.e("=== STATUS_PAUSED")
                            onDownloadPauseOrRunning()
                        }
                        STATUS_SUCCESSFUL -> {
                            Timber.e("=== STATUS_SUCCESSFUL")
                            onDownloadSuccess(pdfSavedPath)
                        }
                        STATUS_FAILED -> {
                            Timber.e("=== STATUS_FAILED")
                            onDownloadFailed(context.getString(R.string.failed_to_download))
                        }
                    }

                    withContext(Dispatchers.IO) {
                        if (status == STATUS_FAILED || status == STATUS_SUCCESSFUL) {
                            Timber.e("=== lifecycleScope.cancel()")
                            lifecycleScope.cancel()
                        }
                        Timber.e("=== cursor.close()")
                        cursor.close()
                    }
                }
            }
        } else {
            Timber.e("=== else onDownloadSuccess")
            onDownloadSuccess(pdfSavedPath)
        }
    }
}