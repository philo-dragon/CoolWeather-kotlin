package com.pfl.coolweather.ui.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.pfl.coolweather.R
import com.pfl.coolweather.db.Weather
import com.pfl.coolweather.util.HttpUtil
import com.pfl.coolweather.util.Utility
import kotlinx.android.synthetic.main.activity_weather.*
import kotlinx.android.synthetic.main.aqi.*
import kotlinx.android.synthetic.main.forecast.*
import kotlinx.android.synthetic.main.now.*
import kotlinx.android.synthetic.main.suggesting.*
import kotlinx.android.synthetic.main.title.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class WeatherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 21) {
            var decordView = window.decorView
            decordView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN; View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }

        setContentView(R.layout.activity_weather)

        var prefs = PreferenceManager.getDefaultSharedPreferences(this)
        var weatherString: String? = prefs.getString("weather", null)
        var bingPic: String? = prefs.getString("bing_pic", null)

        var weatherId: String?

        if (null != weatherString) {
            var weatehr: Weather = Utility.handleWeatherResponse(weatherString)
            weatherId = weatehr.basic!!.id
            showWeatherInfo(weatehr)
        } else {
            weatherId = intent.getStringExtra("weather_id")
            sv_weather.visibility = View.INVISIBLE
            requestWeather(weatherId)
        }
        if (null != bingPic) {
            Glide.with(this@WeatherActivity).load(bingPic).into(img_backgroud)
        } else {
            loadBingPic()
        }


        //为SwipeRefreshLayout设置刷新时的颜色变化，最多可以设置4种
        swipefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light)

        swipefreshLayout.setOnRefreshListener { requestWeather(weatherId) }
        tv_nev.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }

    }

    fun getDrawerLayout(): DrawerLayout {

        return drawerLayout
    }

    fun getSwipefreshLayout(): SwipeRefreshLayout {

        return swipefreshLayout
    }

    /**
     * 根据天气id 请求城市天气信息
     */
    fun requestWeather(weatherId: String?) {

        var weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=0dff9c2f64ca467088a0853001b26d4d"
        HttpUtil.sendOkHttpRequest(weatherUrl, object : Callback {

            override fun onResponse(call: Call?, response: Response?) {
                var responstText = response?.body()?.string()
                var weather: Weather = Utility.handleWeatherResponse(responstText!!)
                runOnUiThread(object : Runnable {
                    override fun run() {
                        if (null != weather && "ok".equals(weather.status)) {
                            var editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                            editor.putString("weather", responstText)
                            editor.apply()
                            showWeatherInfo(weather)
                        } else {
                            Toast.makeText(this@WeatherActivity, "获取天气信息失败", Toast.LENGTH_SHORT).show()
                        }

                        swipefreshLayout.isRefreshing = false
                    }
                })
            }

            override fun onFailure(call: Call?, e: IOException?) {
                runOnUiThread(object : Runnable {
                    override fun run() {
                        Toast.makeText(this@WeatherActivity, "获取天气信息失败", Toast.LENGTH_SHORT).show()
                        swipefreshLayout.isRefreshing = false
                    }
                })
            }

        })


        loadBingPic()
    }

    /**
     * 加载每日一图
     */
    private fun loadBingPic() {

        val weatherUrl = "http://guolin.tech/api/bing_pic"
        HttpUtil.sendOkHttpRequest(weatherUrl, object : Callback {

            override fun onResponse(call: Call?, response: Response?) {
                val bingPic = response!!.body().string()
                var editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                editor.putString("bing_pic", bingPic)
                editor.apply()
                runOnUiThread(object : Runnable {
                    override fun run() {
                        Glide.with(this@WeatherActivity).load(bingPic).into(img_backgroud)
                    }
                })
            }

            override fun onFailure(call: Call?, e: IOException?) {
                e!!.printStackTrace()
            }

        })

    }

    /**
     * 处理并展示 Weather实体类中的数据
     */

    private fun showWeatherInfo(weatehr: Weather) {

        tv_city.text = weatehr.basic!!.city
        tv_update_time.text = weatehr.basic!!.update!!.loc!!.split(" ")[1]
        tv_degree.text = weatehr.now!!.tmp + "℃"
        tv_wetherinfo.text = weatehr.now!!.cond!!.txt
        ll_forecast.removeAllViews()
        weatehr.daily_forecast!!.forEach {
            var view = getLayoutInflater().inflate(R.layout.item_forecast, ll_forecast, false)
            view.findViewById<TextView>(R.id.tv_date).text = it.date
            view.findViewById<TextView>(R.id.tv_info).text = it.cond!!.txt_n
            view.findViewById<TextView>(R.id.tv_max).text = it.tmp!!.max
            view.findViewById<TextView>(R.id.tv_min).text = it.tmp!!.min

            ll_forecast.addView(view)
        }

        tv_aqi.text = weatehr.aqi!!.city!!.aqi
        tv_pm25.text = weatehr.aqi!!.city!!.pm25
        tv_comfort.text = "舒适度: " + weatehr.suggestion!!.comf!!.txt
        tv_car_wash.text = "洗车指数: " + weatehr.suggestion!!.cw!!.txt
        tv_sport.text = "运动建议: " + weatehr.suggestion!!.sport!!.txt
        sv_weather.visibility = View.VISIBLE
    }
}
