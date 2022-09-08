package com.example.download_manager

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.button.MaterialButton
import java.io.File

class MainActivity : AppCompatActivity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //declare CustomDownloadManager
        val customDownloadManager = CustomDownloadManager()

        //declare pdfView
        val pdfView = findViewById<PDFView>(R.id.pdf_view)


        //declare url
        val downloadUrl =
            "http://www.africau.edu/images/default/${CustomDownloadManager.PDF_NAME}${CustomDownloadManager.PDF_EXTENSION}"

        //declare button
        val button = findViewById<MaterialButton>(R.id.btn_download)
        button.setOnClickListener {
            customDownloadManager.downloadPdf(
                context = applicationContext,
                lifecycleScope = lifecycleScope,
                pdfUrl = downloadUrl,
                onDownloadFailed = {
                    showToast(it)
                },
                onDownloadPauseOrRunning = {

                },
                onDownloadSuccess = {
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