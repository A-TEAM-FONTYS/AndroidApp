package com.example.appusagedata.fragments

import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.appusagedata.MainActivity
import com.example.appusagedata.R
import com.example.appusagedata.adapters.CustomListAdapter
import com.example.appusagedata.models.AppStatData
import com.example.appusagedata.viewmodels.DataViewModel
import kotlinx.android.synthetic.main.data_fragment.*
import okhttp3.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit


class DataFragment : Fragment() {

    var AppStatDataList = mutableListOf<AppStatData>()
    lateinit var chartDataUsage: AnyChartView
    lateinit var layoutChart: LinearLayout
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
        chartDataUsage = view?.findViewById(R.id.chart_data_usage)!!
        layoutChart = view?.findViewById(R.id.layout_chart_data_usage)!!
        lstSocialMedia = listOf(*resources.getStringArray(R.array.social_media_array))


        get_app_usage_stats_btn.setOnClickListener {
            showUsageStats()
        }

        var chart_btn: LinearLayout = view?.findViewById(R.id.get_app_usage_stats_btn)!!
        val ttb = AnimationUtils.loadAnimation(this.context, R.anim.top_to_bottom)
        chart_btn.startAnimation(ttb)
        chart_btn.visibility = View.VISIBLE
        super.onStart()
    }

    private fun showUsageStats() {
        var usageStatManager: UsageStatsManager = this.context?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val start = LocalDate.now().atStartOfDay(ZoneId.of("CET")).toInstant().toEpochMilli()
        val end = ZonedDateTime.now().toInstant().toEpochMilli()

        var queryUsageStats: List<UsageStats> = usageStatManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end)


        var statsData = ""
        for(i in queryUsageStats.indices){
            // If app time is more than 0 minutes
            if(TimeUnit.MILLISECONDS.toMinutes(queryUsageStats.get(i).totalTimeInForeground) > 0){
                var appStatData = AppStatData(this.context!!.packageManager.getApplicationLabel(this.context!!.packageManager.getApplicationInfo(queryUsageStats.get(i).packageName, 0)).toString(), queryUsageStats.get(i).totalTimeInForeground)
                // Check if app is a social media app
                if(isSocialMedia(appStatData.appName.toUpperCase())){
                    if(!doesAppStatExist(appStatData)){
                        AppStatDataList.add(appStatData)

                    }
                }
            }
        }

        setPieChart(AppStatDataList)
        showTableStats()
        saveDataToDatabase(AppStatDataList)
    }

    private fun doesAppStatExist(appStatData: AppStatData): Boolean {
        var result = false

        AppStatDataList.forEach { appStat ->
            if(appStat.appName == appStatData.appName){
                result = true
            }
        }

        return result
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


    private fun saveDataToDatabase(lstAppStatData: MutableList<AppStatData>){
        lstAppStatData.forEach { appStat ->
            (activity as MainActivity?)?.saveDataStat(appStat)
        }
    }
    private fun setPieChart(lstAppStatData: MutableList<AppStatData>){
        var pie: Pie = AnyChart.pie()
        var dataEntries: MutableList<DataEntry> = ArrayList()
        var colorPallete :Array<String> = arrayOf("#D6D4AF", "#B6B99C", "#969E88", "#778475", "#576961", "#475B58")

        lstAppStatData.forEach { appStat ->
            dataEntries.add(ValueDataEntry(appStat.appName, appStat.timeUsed))
        }

        pie.data(dataEntries)
        pie.legend(false)
        pie.tooltip().format("Time spent: {%value} minutes")
        pie.background().fill("#273A3A")
        pie.palette(colorPallete)
        chartDataUsage.setChart(pie)

        val ttb = AnimationUtils.loadAnimation(this.context, R.anim.top_to_bottom)
        layoutChart.startAnimation(ttb)
        layoutChart.visibility = View.VISIBLE
    }

    private fun showTableStats(){
        val adapter = CustomListAdapter(activity,
            AppStatDataList as java.util.ArrayList<AppStatData>?
        )
        var listView: ListView = view?.findViewById(R.id.list_data_stats)!!
        listView.setAdapter(adapter)


        var layoutList: LinearLayout= view?.findViewById(R.id.layout_list_data)!!
        val ttb = AnimationUtils.loadAnimation(this.context, R.anim.top_to_bottom)
        layoutList.startAnimation(ttb)
        layoutList.visibility = View.VISIBLE
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DataViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
