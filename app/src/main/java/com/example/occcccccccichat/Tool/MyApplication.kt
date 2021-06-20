package com.example.occcccccccichat.Tool

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.starrtc.starrtcsdk.api.XHCustomConfig

class MyApplication : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}