package com.example.appusagedata.models

import java.util.concurrent.TimeUnit

class AppStatData(packageName: String, timeUsed: Long) {
    var appName: String = ""
    var timeUsed: Long = 0

    init{
        this.appName = getAppNameFromPackageName(packageName)
        this.timeUsed = convertToMinutes(timeUsed)
    }

    private fun convertToMinutes(time: Long): Long{
        return  TimeUnit.MILLISECONDS.toMinutes(time)
    }

    private fun getAppNameFromPackageName(packageName: String): String{
        val packageNameSplit = packageName.split(".")
        val name =  packageNameSplit.get(packageNameSplit.count()-1)

        //Return with first letter to upper
        return name[0].toUpperCase()+name.substring(1)
    }

}