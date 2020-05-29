package com.example.appusagedata.handlers

import android.os.Handler
import android.os.HandlerThread
import okhttp3.OkHttpClient

class AppStatDataHandler : HandlerThread("AppStatDataHandler") {

    private val TAG: String = "AppStatDataHandler"
    private var handler: Handler? = null

    private var AppStatDataThread: HandlerThread? = null
    private var AppStatDataHandler: Handler? = null

    private val client = OkHttpClient()
}