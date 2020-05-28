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
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var tvUsageStats: TextView
    lateinit var lstSocialMedia: List<String>
    var AppStatDataList = mutableListOf<AppStatData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvUsageStats = findViewById(R.id.tvUsageStats)

        if(checkUsageStatsPermission()){
            lstSocialMedia = listOf(*resources.getStringArray(R.array.social_media_array))
        }
        else{
            //Navigate to settings to set permissions
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }

        get_app_usage_stats_btn.setOnClickListener {
            showUsageStats()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showUsageStats() {
        var usageStatManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val start = LocalDate.now().atStartOfDay(ZoneId.of("CET")).toInstant().toEpochMilli()
        val end = ZonedDateTime.now().toInstant().toEpochMilli()

        var queryUsageStats: List<UsageStats> = usageStatManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)

        var statsData = ""
        for(i in queryUsageStats.indices){
            // If app time is more than 0 minutes
            if(TimeUnit.MILLISECONDS.toMinutes(queryUsageStats.get(i).totalTimeInForeground) > 0){
               var appStatData = AppStatData(this.packageManager.getApplicationLabel(this.packageManager.getApplicationInfo(queryUsageStats.get(i).packageName, 0)).toString(), queryUsageStats.get(i).totalTimeInForeground)
                // Check if app is a social media app
                if(isSocialMedia(appStatData.appName)){
                    AppStatDataList.add(appStatData)
                    statsData = statsData + "App: " + appStatData.appName + "\n" +
                            "Time used: " + appStatData.timeUsed + " minutes\n\n"
                }
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

    private fun isSocialMedia(packageName: String): Boolean {
        lstSocialMedia.forEach {
                service ->
            if (packageName.contains(service)){
                return true
            }
        }

        return false
    }
}
