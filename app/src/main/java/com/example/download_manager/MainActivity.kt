package com.example.download_manager

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.download_manager.util.DownloadHelper
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.button.MaterialButton
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private val yourViewModel: MainViewModel by viewModels()

    private lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //declare lottie
        val lottie: LottieAnimationView  = findViewById(R.id.lav)

        //declare pdfView
        pdfView = findViewById(R.id.pdf_view)

        //declare button
        val button = findViewById<MaterialButton>(R.id.btn_download)
        button.setOnClickListener {
            lottie.visibility = View.VISIBLE

            /**
             * if file already exist in directory, dont download, show pdf file
             */
            if (!DownloadHelper.getFile(applicationContext).exists()) {
                Timber.e("*** File not Exist")
                yourViewModel.makeRequest(
                    context = applicationContext,
                    onFailed = {
                        Timber.e("*** onFailed")
                        lottie.visibility = View.GONE
                        showToast(applicationContext.getString(it))
                    },
                    onSuccess = {
                        Timber.e("*** onSuccess")

                        lottie.visibility = View.GONE
                        showInPdfViewer()
                    },
                    onLoading = {
                        Timber.e("*** onLoading")
                        lottie.visibility = View.VISIBLE
                    })
            } else {
                Timber.e("*** File Exist")
                lottie.visibility = View.GONE
                showInPdfViewer()
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

    private fun showInPdfViewer() {
        pdfView.fromFile(DownloadHelper.getFile(applicationContext)).load()
    }
}