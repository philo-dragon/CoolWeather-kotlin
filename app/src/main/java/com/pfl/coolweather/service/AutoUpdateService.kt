package com.pfl.coolweather.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.IBinder
import android.os.SystemClock
import android.preference.PreferenceManager
import android.support.annotation.IntDef

import com.pfl.coolweather.db.Weather
import com.pfl.coolweather.util.HttpUtil
import com.pfl.coolweather.util.Utility

import java.io.IOException

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

class AutoUpdateService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        updateWeather()
        updateBingPic()

        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val anHour = 8 * 60 * 60 * 1000
        val triggerAtTime = SystemClock.elapsedRealtime() + anHour

        val i = Intent(this, AutoUpdateService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, i, 0)
        manager.cancel(pendingIntent)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent)

        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 更新天气信息
     */

    private fun updateWeather() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val weaterString = prefs.getString("weater", null)
        if (null != weaterString) {
            val weather = Utility.handleWeatherResponse(weaterString)
            val weatherId = weather!!.basic!!.id

            val weatherUrl = "http://guolin.tech/api/weather?cityid=$weatherId&key=0dff9c2f64ca467088a0853001b26d4d"
            HttpUtil.sendOkHttpRequest(weatherUrl, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {

                    val responseText = response.body().string()
                    val weather = Utility.handleWeatherResponse(responseText)
                    if (null != weather && "ok" == weather.status) {
                        val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                        editor.putString("weather", responseText)
                        editor.apply()
                    }
                }
            })

        }
    }

    /**
     * 更新每日一图
     */

    private fun updateBingPic() {
        val weatherUrl = "http://guolin.tech/api/bing_pic"

        HttpUtil.sendOkHttpRequest(weatherUrl, object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val bingPic = response.body().string()
                val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                editor.putString("bing_pic", bingPic)
                editor.apply()
            }
        })
    }
}
