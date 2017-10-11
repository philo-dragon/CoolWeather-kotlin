package com.pfl.coolweather.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import com.pfl.coolweather.MainActivity
import com.pfl.coolweather.R
import com.pfl.coolweather.db.City
import com.pfl.coolweather.db.County
import com.pfl.coolweather.db.Province
import com.pfl.coolweather.ui.activity.WeatherActivity
import com.pfl.coolweather.util.HttpUtil
import com.pfl.coolweather.util.Utility

import org.litepal.crud.DataSupport

import java.io.IOException
import java.util.ArrayList

import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response

/**
 * Created by Administrator on 2017/10/6 0006.
 */

class ChooseAreaFragment : Fragment() {


    private var currentLevel = LEVEL_PROVINCE

    private var tvTitle: TextView? = null
    private var tvBack: TextView? = null
    private var lvListView: ListView? = null


    private var adapter: ArrayAdapter<String>? = null
    private val dataList = ArrayList<String>()

    /**
     * 省列表
     */
    private var provinceList: List<Province>? = null

    /**
     * 市列表
     */
    private var cityList: List<City>? = null

    /**
     * 縣列表
     */
    private var countyList: List<County>? = null

    private var selectedProvince: Province? = null
    private var selectedCity: City? = null
    private var progressDialog: ProgressDialog? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_choose_area, container, false)

        tvTitle = view.findViewById<View>(R.id.tvTitle) as TextView
        tvBack = view.findViewById<View>(R.id.btnBack) as TextView
        lvListView = view.findViewById<View>(R.id.lvListView) as ListView

        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, dataList)

        lvListView!!.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        lvListView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            if (currentLevel == LEVEL_PROVINCE) {
                selectedProvince = provinceList!![i]
                queryCities()
            } else if (currentLevel == LEVEL_CITY) {
                selectedCity = cityList!![i]
                queryCounties()
            } else if (currentLevel == LEVEL_COUNTY) {
                val weatherId = countyList!![i].weatherId
                if (activity is MainActivity) {
                    val intent = Intent(activity, WeatherActivity::class.java)
                    intent.putExtra("weather_id", weatherId)
                    startActivity(intent)
                    activity.finish()
                } else if (activity is WeatherActivity) {
                    val weatherActivity = activity as WeatherActivity
                    weatherActivity.getDrawerLayout().closeDrawers()
                    weatherActivity.getSwipefreshLayout().isRefreshing = true
                    weatherActivity.requestWeather(weatherId)
                }
            }
        }

        tvBack!!.setOnClickListener {
            if (currentLevel == LEVEL_COUNTY) {
                queryCities()
            } else if (currentLevel == LEVEL_CITY) {
                queryProvincies()
            }
        }

        queryProvincies()
    }

    private fun queryProvincies() {
        tvTitle!!.text = "中国"
        tvBack!!.visibility = View.GONE
        provinceList = DataSupport.findAll(Province::class.java)
        if (provinceList!!.isEmpty()) {

            val address = "http://guolin.tech/api/china"
            queryFromServer(address, "province")

        } else {

            dataList.clear()
            for (province in provinceList!!) {
                dataList.add(province.name!!)
            }

            adapter!!.notifyDataSetChanged()
            lvListView!!.setSelection(0)
            currentLevel = LEVEL_PROVINCE
        }
    }

    private fun queryCities() {
        tvTitle!!.text = selectedProvince!!.name
        tvBack!!.visibility = View.VISIBLE

        cityList = DataSupport.where("provinceId = ?", selectedProvince!!.provinceCode.toString()).find(City::class.java)

        if (cityList!!.isEmpty()) {
            val address = "http://guolin.tech/api/china/" + selectedProvince!!.provinceCode
            queryFromServer(address, "city")
        } else {
            dataList.clear()
            for (city in cityList!!) {
                dataList.add(city.name!!)
            }

            adapter!!.notifyDataSetChanged()
            lvListView!!.setSelection(0)
            currentLevel = LEVEL_CITY
        }
    }

    private fun queryCounties() {

        tvTitle!!.text = selectedCity!!.name
        tvBack!!.visibility = View.VISIBLE

        countyList = DataSupport.where("cityid = ?", selectedCity!!.cityCode.toString()).find(County::class.java)

        if (countyList!!.isEmpty()) {
            val address = "http://guolin.tech/api/china/" + selectedProvince!!.provinceCode + "/" + selectedCity!!.cityCode
            queryFromServer(address, "county")
        } else {

            dataList.clear()
            for (county in countyList!!) {
                dataList.add(county.name!!)
            }

            adapter!!.notifyDataSetChanged()
            lvListView!!.setSelection(0)
            currentLevel = LEVEL_COUNTY
        }
    }


    private fun queryFromServer(address: String, type: String) {

        showProgressDialog()


        HttpUtil.sendOkHttpRequest(address, object : Callback {

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {

                val reponseText = response.body().string()
                var result = false
                if ("province" == type) {
                    result = Utility.handleProvinceResponse(reponseText)
                } else if ("city" == type) {
                    result = Utility.handleCityResponse(reponseText, selectedProvince!!.provinceCode)
                } else if ("county" == type) {
                    result = Utility.handleCountyResponse(reponseText, selectedCity!!.cityCode)
                }

                if (result) {
                    activity.runOnUiThread {
                        closeProgressDialog()

                        if ("province" == type) {
                            queryProvincies()
                        } else if ("city" == type) {
                            queryCities()
                        } else if ("county" == type) {
                            queryCounties()
                        }
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {

                activity.runOnUiThread {
                    closeProgressDialog()
                    Toast.makeText(context.applicationContext, "加载失败", Toast.LENGTH_SHORT).show()
                }

            }

        })

    }

    private fun showProgressDialog() {

        if (null == progressDialog) {
            progressDialog = ProgressDialog(activity)
            progressDialog!!.setMessage("正在加载...")
            progressDialog!!.setCanceledOnTouchOutside(false)
        }
        progressDialog!!.show()
    }

    private fun closeProgressDialog() {
        if (null != progressDialog) {
            progressDialog!!.dismiss()
        }
    }

    companion object {
        //伴生对象 使用方式相当于java文件中的 static final
        private val LEVEL_PROVINCE = 0
        private val LEVEL_CITY = 1
        private val LEVEL_COUNTY = 2
    }
}
