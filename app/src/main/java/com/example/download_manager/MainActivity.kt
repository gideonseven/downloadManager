package com.example.download_manager

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.button.MaterialButton
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lottie = findViewById<LottieAnimationView>(R.id.lav)
        //declare CustomDownloadManager
        val customDownloadManager = CustomDownloadManager()

        //declare pdfView
        val pdfView = findViewById<PDFView>(R.id.pdf_view)

        //declare url
        val downloadUrl =
            "https://unec.edu.az/application/uploads/2014/12/${CustomDownloadManager.PDF_NAME}${CustomDownloadManager.PDF_EXTENSION}"

        //declare button
        val button = findViewById<MaterialButton>(R.id.btn_download)
        button.setOnClickListener {
            customDownloadManager.downloadPdf(
                context = applicationContext,
                lifecycleScope = lifecycleScope,
                pdfUrl = downloadUrl,
                onDownloadFailed = {
                    showToast(it)
                    lottie.visibility = View.GONE
                    button.visibility = View.VISIBLE
                },
                onDownloadPauseOrRunning = {
                    lottie.visibility = View.VISIBLE
                    button.visibility = View.GONE
                },
                onDownloadSuccess = {
                    lottie.visibility = View.GONE
                    button.visibility = View.VISIBLE
                    val file = File(it)
                    pdfView.fromFile(file)
                        .load()
                }
            )
        }


    }

    private fun showToast(info: String) {
        Toast.makeText(applicationContext, info, Toast.LENGTH_SHORT).show()
    }
}