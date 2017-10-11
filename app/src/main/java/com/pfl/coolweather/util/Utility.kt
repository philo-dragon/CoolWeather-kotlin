package com.pfl.coolweather.util

import android.text.TextUtils

import com.google.gson.Gson
import com.pfl.coolweather.db.City
import com.pfl.coolweather.db.County
import com.pfl.coolweather.db.Province
import com.pfl.coolweather.db.Weather
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by Administrator on 2017/10/6 0006.
 */

object Utility {


    /**
     * 解析和处理服务器返回的省级数据
     *
     * @param response
     * @return
     */
    fun handleProvinceResponse(response: String): Boolean {

        if (!TextUtils.isEmpty(response)) {

            try {
                val allProvince = JSONArray(response)
                for (i in 0 until allProvince.length()) {
                    val provinceObject = allProvince.getJSONObject(i)
                    val province = Province()
                    province.name = provinceObject.getString("name")
                    province.provinceCode = provinceObject.getInt("id")
                    province.save()
                }
                return true
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        return false
    }

    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param response
     * @return
     */
    fun handleCityResponse(response: String, provinceId: Int): Boolean {

        if (!TextUtils.isEmpty(response)) {

            try {
                val allCitys = JSONArray(response)
                for (i in 0 until allCitys.length()) {
                    val cityObject = allCitys.getJSONObject(i)
                    val city = City()
                    city.name = cityObject.getString("name")
                    city.cityCode = cityObject.getInt("id")
                    city.provinceId = provinceId
                    city.save()
                }
                return true
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        return false
    }

    /**
     * 解析和处理服务器返回的县级数据
     *
     * @param response
     * @return
     */
    fun handleCountyResponse(response: String, cityId: Int): Boolean {

        if (!TextUtils.isEmpty(response)) {

            try {
                val allCountys = JSONArray(response)
                for (i in 0 until allCountys.length()) {
                    val countyObject = allCountys.getJSONObject(i)
                    val county = County()
                    county.weatherId = countyObject.getString("weather_id")
                    county.name = countyObject.getString("name")
                    county.contyCode = countyObject.getInt("id")
                    county.cityId = cityId
                    county.save()
                }

                return true
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        return false
    }

    /**
     * 将返回的 JSON 数据解析成 Weather 实体类
     *
     * @param weatherString
     * @return
     */
    fun handleWeatherResponse(weatherString: String): Weather? { // 加？表示可返回null数据

        try {
            val jsonObject = JSONObject(weatherString)
            val jsonArray = jsonObject.getJSONArray("HeWeather")
            val weatherContent = jsonArray.getJSONObject(0).toString()
            return Gson().fromJson(weatherContent, Weather::class.java)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}
