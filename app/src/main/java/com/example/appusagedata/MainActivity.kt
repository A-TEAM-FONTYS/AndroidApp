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
import android.widget.TextView
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    lateinit var tvUsageStats: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvUsageStats = findViewById(R.id.tvUsageStats)
        if(checkUsageStatsPermission()){
            showUsageStats()
        }
        else{
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showUsageStats() {
        var usageStatManager: UsageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        var cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("CET"))
        cal.add(Calendar.DAY_OF_MONTH, -1)

        val start = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = ZonedDateTime.now().toInstant().toEpochMilli()

//        var queryUsageStats: List<UsageStats> = usageStatManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, cal.timeInMillis,
//        System.currentTimeMillis())

        var queryUsageStats: List<UsageStats> = usageStatManager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY, start, end)

        var statsData: String = ""
        for(i in 0..queryUsageStats.size-1){
            if(TimeUnit.MILLISECONDS.toMinutes(queryUsageStats.get(i).totalTimeInForeground) > 0){
                statsData = statsData + "Packagename: " + queryUsageStats.get(i).packageName + "\n" +
                        "Last time used: " + convertTime(queryUsageStats.get(i).lastTimeUsed) + "\n" +
                        "Describe contents: " + queryUsageStats.get(i).describeContents()    + "\n" +
                        "First time stamp: " + convertTime(queryUsageStats.get(i).firstTimeStamp) + "\n" +
                        "Last time stamp: " + convertTime(queryUsageStats.get(i).lastTimeStamp) + "\n" +
                        "Total time foreground: " + TimeUnit.MILLISECONDS.toMinutes(queryUsageStats.get(i).totalTimeInForeground) + " minutes \n\n"
            }
        }


        tvUsageStats.setText(statsData)
    }

    private fun convertTime(lastTimeUsed: Long): String {
        var date: Date = Date(lastTimeUsed)
        var format: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH)
        format.setTimeZone(TimeZone.getTimeZone("CET"))

        return format.format(date)
    }

    private fun checkUsageStatsPermission(): Boolean {
        var appOpsManager: AppOpsManager ? = null
        var mode: Int = 0

        appOpsManager = getSystemService(Context.APP_OPS_SERVICE)!! as AppOpsManager

        mode = appOpsManager.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
        return mode  == MODE_ALLOWED
    }
}
