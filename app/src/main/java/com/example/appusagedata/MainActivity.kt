package com.example.appusagedata

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.appusagedata.models.AppStatData
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var tvUsageStats: TextView
    var AppStatDataList = mutableListOf<AppStatData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvUsageStats = findViewById(R.id.tvUsageStats)

        if(checkUsageStatsPermission()){
            showUsageStats()
        }
        else{
            //Navigate to settings to set permissions
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showUsageStats() {
        var usageStatManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = ZonedDateTime.now().toInstant().toEpochMilli()\

        var queryUsageStats: List<UsageStats> = usageStatManager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY, start, end)

        var statsData: String = ""
        for(i in 0..queryUsageStats.size-1){
            if(TimeUnit.MILLISECONDS.toMinutes(queryUsageStats.get(i).totalTimeInForeground) > 0){
               var appStatData: AppStatData = AppStatData(queryUsageStats.get(i).packageName, queryUsageStats.get(i).totalTimeInForeground)
                AppStatDataList.add(appStatData)
                statsData = statsData + "App: " + appStatData.appName + "\n" +
                                        "Time used: " + appStatData.timeUsed + " minutes\n\n"
            }
        }
        tvUsageStats.setText(statsData)
    }

    private fun checkUsageStatsPermission(): Boolean {
        var appOpsManager: AppOpsManager ? = null
        var mode: Int = 0

        appOpsManager = getSystemService(Context.APP_OPS_SERVICE)!! as AppOpsManager

        mode = appOpsManager.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
        return mode  == MODE_ALLOWED
    }
}
