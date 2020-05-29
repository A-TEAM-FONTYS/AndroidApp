package com.example.appusagedata

import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.app.AppOpsManager.OPSTR_GET_USAGE_STATS
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.appusagedata.fragments.DataFragment
import com.example.appusagedata.fragments.SignInFragment
import com.example.appusagedata.fragments.SignUpFragment
import com.example.appusagedata.models.AppStatData
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportActionBar()?.hide()
        loadFragment(SignUpFragment())

        if(!checkUsageStatsPermission()){
            //Navigate to settings to set permissions
            startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .commit()

            return true
        }
        return false
    }

    private fun checkUsageStatsPermission(): Boolean {
        var appOpsManager: AppOpsManager? = null
        var mode: Int = 0

        appOpsManager = getSystemService(Context.APP_OPS_SERVICE)!! as AppOpsManager

        mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
        return mode  == AppOpsManager.MODE_ALLOWED
    }
}
