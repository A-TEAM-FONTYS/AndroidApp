package com.example.appusagedata.fragments

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.appusagedata.viewmodels.DataViewModel
import com.example.appusagedata.R
import com.example.appusagedata.models.AppStatData
import kotlinx.android.synthetic.main.data_fragment.*
import okhttp3.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit


class DataFragment : Fragment() {


    lateinit var tvUsageStats: TextView
    lateinit var lstSocialMedia: List<String>

    companion object {
        fun newInstance() = DataFragment()
    }

    private lateinit var viewModel: DataViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.data_fragment, container, false)
    }

    override fun onStart(){
        tvUsageStats = view?.findViewById(R.id.tvUsageStats)!!
        lstSocialMedia = listOf(*resources.getStringArray(R.array.social_media_array))


        get_app_usage_stats_btn.setOnClickListener {
            showUsageStats()
        }
        super.onStart()
    }

    private fun showUsageStats() {
        var usageStatManager: UsageStatsManager = this.context?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val start = LocalDate.now().atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
        val end = ZonedDateTime.now().toInstant().toEpochMilli()

        var queryUsageStats: List<UsageStats> = usageStatManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)

        var AppStatDataList = mutableListOf<AppStatData>()
        var statsData = ""
        for(i in queryUsageStats.indices){
            // If app time is more than 0 minutes
            if(TimeUnit.MILLISECONDS.toMinutes(queryUsageStats.get(i).totalTimeInForeground) > 0){
                var appStatData = AppStatData(this.context!!.packageManager.getApplicationLabel(this.context!!.packageManager.getApplicationInfo(queryUsageStats.get(i).packageName, 0)).toString(), queryUsageStats.get(i).totalTimeInForeground)
                // Check if app is a social media app
                if(isSocialMedia(appStatData.appName.toUpperCase())){
                    AppStatDataList.add(appStatData)
                    statsData = statsData + "App: " + appStatData.appName + "\n" +
                            "Time used: " + appStatData.timeUsed + " minutes\n\n"
                }
            }
        }

        AppStatDataList = removeDoublesFromList(AppStatDataList)
//        saveDataToDatabase(AppStatDataList)

        tvUsageStats.setText(statsData)
    }

    private fun removeDoublesFromList(lstAppStatData: MutableList<AppStatData>): MutableList<AppStatData>{
        return lstAppStatData.toSet().toMutableList()
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

    private fun createRequestBody(appData : AppStatData): RequestBody {
        return FormBody.Builder()
            .add("appName", appData.appName)
            .add("timeUsed", appData.timeUsed.toString())
            .build()
    }

    private fun postData(appData : AppStatData) {
        val request = Request.Builder()
            .url("http://localhost:9092/data")
            .post(createRequestBody(appData))
            .build()


        val call: Call = OkHttpClient().newCall(request)
        val response: Response = call.execute()

        Log.d("Post", response.toString())
    }

    private fun saveDataToDatabase(lstAppStatData: MutableList<AppStatData>){
        lstAppStatData.forEach { appStat ->
            postData(appStat)
        }

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
