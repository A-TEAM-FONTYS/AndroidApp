package com.example.appusagedata.models

import java.util.concurrent.TimeUnit

class AppStatData(packageName: String, timeUsed: Long) {
    var appName: String = ""
    var timeUsed: Long = 0

    init{
        this.appName = packageName
        this.timeUsed = convertToMinutes(timeUsed)
    }

    private fun convertToMinutes(time: Long): Long{
        return  TimeUnit.MILLISECONDS.toMinutes(time)
    }

}