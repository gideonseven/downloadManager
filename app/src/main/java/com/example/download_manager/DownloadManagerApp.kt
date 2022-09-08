package com.example.download_manager

import android.app.Application
import timber.log.Timber


/**
 * Created by gideon on 9/8/2022
 * gideon@cicil.co.id
 * https://www.cicil.co.id/
 */
class DownloadManagerApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Logging
        Timber.plant(Timber.DebugTree())
    }
}