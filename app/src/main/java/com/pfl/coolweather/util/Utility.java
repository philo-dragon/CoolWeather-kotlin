package com.pfl.coolweather.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.pfl.coolweather.db.City;
import com.pfl.coolweather.db.County;
import com.pfl.coolweather.db.Province;
import com.pfl.coolweather.db.Weather;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/10/6 0006.
 */

public class Utility {


    /**
     * 解析和处理服务器返回的省级数据
     *
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response) {

        if (!TextUtils.isEmpty(response)) {

            try {
                JSONArray allProvince = new JSONArray(response);
                for (int i = 0; i < allProvince.length(); i++) {
                    JSONObject provinceObject = allProvince.getJSONObject(i);
                    Province province = new Province();
                    province.setName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    province.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param response
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceId) {

        if (!TextUtils.isEmpty(response)) {

            try {
                JSONArray allCitys = new JSONArray(response);
                for (int i = 0; i < allCitys.length(); i++) {
                    JSONObject cityObject = allCitys.getJSONObject(i);
                    City city = new City();
                    city.setName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     *
     * @param response
     * @return
     */
    public static boolean handleCountyResponse(String response, int cityId) {

        if (!TextUtils.isEmpty(response)) {

            try {
                JSONArray allCountys = new JSONArray(response);
                for (int i = 0; i < allCountys.length(); i++) {
                    JSONObject countyObject = allCountys.getJSONObject(i);
                    County county = new County();
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setName(countyObject.getString("name"));
                    county.setContyCode(countyObject.getInt("id"));
                    county.setCityId(cityId);
                    county.save();
                }

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     * 将返回的 JSON 数据解析成 Weather 实体类
     *
     * @param weatherString
     * @return
     */
    @NotNull
    public static Weather handleWeatherResponse(@NotNull String weatherString) {

        try {
            JSONObject jsonObject = new JSONObject(weatherString);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
