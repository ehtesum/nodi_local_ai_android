package com.localai.nodi

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NodiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
