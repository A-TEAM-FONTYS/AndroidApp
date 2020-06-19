package com.example.appusagedata

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.appusagedata.fragments.DataFragment
import com.example.appusagedata.fragments.SignInFragment
import com.example.appusagedata.handlers.LoginHandler
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity()  {
    private lateinit var token: JSONObject
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSupportActionBar()?.hide()
        loadFragment(SignInFragment())

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

    fun login(email: String, pwd: String){
        var result = LoginHandler().execute(email, pwd).get()

        if(isJSONValid(result)){
            token = JSONObject(result)
            loadFragment(DataFragment())
        }
        else{
            val toast = Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    private fun isJSONValid(json: String?): Boolean {
        try {
            JSONObject(json)
        } catch (ex: JSONException) {
            try {
                JSONArray(json)
            } catch (ex1: JSONException) {
                return false
            }
        }
        return true
    }


    private fun checkUsageStatsPermission(): Boolean {
        var appOpsManager: AppOpsManager? = null
        var mode: Int = 0

        appOpsManager = getSystemService(Context.APP_OPS_SERVICE)!! as AppOpsManager

        mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), packageName)
        return mode  == AppOpsManager.MODE_ALLOWED
    }
}
