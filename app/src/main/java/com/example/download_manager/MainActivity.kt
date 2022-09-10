package com.example.download_manager

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.download_manager.Constant.Companion.EXAMPLE_FOLDER
import com.example.download_manager.Constant.Companion.PDF_EXTENSION
import com.example.download_manager.Constant.Companion.PDF_NAME
import com.example.download_manager.network.RetrofitBuilder
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.button.MaterialButton
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.*


class MainActivity : AppCompatActivity() {
    lateinit var pdfView: PDFView
    lateinit var lottie: LottieAnimationView
    var filePath: String = Constant.TEXT_BLANK
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //declare lottie
        lottie = findViewById(R.id.lav)

        //declare pdfView
        pdfView = findViewById(R.id.pdf_view)

        //declare button
        val button = findViewById<MaterialButton>(R.id.btn_download)
        button.setOnClickListener {
            makeRequest()
        }
    }

    private fun showToast(info: String) {
        Toast.makeText(applicationContext, info, Toast.LENGTH_SHORT).show()
    }

    private fun makeRequest() {
        Timber.e("makeRequest")
        lottie.visibility = View.VISIBLE
        val repos = RetrofitBuilder.service.getPdfFile(PDF_NAME+ PDF_EXTENSION)
        repos.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Timber.e("onResponse ${response.raw()}")
                Timber.e("onResponse ${response.errorBody().toString()}")

                if (response.isSuccessful) {
                    lottie.visibility = View.GONE
                    saveToDisk(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Timber.e("onFailure")
                Timber.e("${t.message}")
                showToast(t.message ?: Constant.TEXT_BLANK)
                lottie.visibility = View.GONE
            }
        })
    }

    fun saveToDisk(body: ResponseBody) {
        try {
            // state pdfSaved Path from Url by generate PdfId
            filePath = applicationContext.getExternalFilesDir(EXAMPLE_FOLDER)
                .toString() + File.separator + PDF_NAME + PDF_EXTENSION
            Timber.e("=== pdfSavedPathEXT  $filePath")
            val destinationFile = File(filePath)
            var `is`: InputStream? = null
            var os: OutputStream? = null
            try {
                Timber.e("File Size=" + body.contentLength())
                `is` = body.byteStream()
                os = FileOutputStream(destinationFile)
                val data = ByteArray(4096)
                var count: Int
                var progress = 0
                while (`is`.read(data).also { count = it } != -1) {
                    os.write(data, 0, count)
                    progress += count
                    Timber.e(
                        "Progress: " + progress + "/" + body.contentLength() + " >>>> " + progress.toFloat() / body.contentLength()
                    )
                }
                os.flush()
                Timber.e("File saved successfully!")
                pdfView.fromFile(File(filePath)).load()
                return
            } catch (e: IOException) {
                e.printStackTrace()
                Timber.e("Failed to save the file!")
                return
            } finally {
                `is`?.close()
                os?.close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Timber.e("Failed to save the file!")
            return
        }
    }
}