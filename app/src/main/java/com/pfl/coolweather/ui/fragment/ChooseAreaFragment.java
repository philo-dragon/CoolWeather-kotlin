package com.pfl.coolweather.ui.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pfl.coolweather.MainActivity;
import com.pfl.coolweather.R;
import com.pfl.coolweather.db.City;
import com.pfl.coolweather.db.County;
import com.pfl.coolweather.db.Province;
import com.pfl.coolweather.ui.activity.WeatherActivity;
import com.pfl.coolweather.util.HttpUtil;
import com.pfl.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/6 0006.
 */

public class ChooseAreaFragment extends Fragment {

    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;


    private int currentLevel = LEVEL_PROVINCE;

    private TextView tvTitle;
    private TextView tvBack;
    private ListView lvListView;


    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 縣列表
     */
    private List<County> countyList;

    private Province selectedProvince;
    private City selectedCity;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_area, container, false);

        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        tvBack = (TextView) view.findViewById(R.id.btnBack);
        lvListView = (ListView) view.findViewById(R.id.lvListView);

        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);

        lvListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = provinceList.get(i);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectedCity = cityList.get(i);
                    queryCounties();
                } else if (currentLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(i).getWeatherId();
                    if (getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(), WeatherActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    } else if (getActivity() instanceof WeatherActivity) {
                        WeatherActivity weatherActivity = (WeatherActivity) getActivity();
                        weatherActivity.getDrawerLayout().closeDrawers();
                        weatherActivity.getSwipefreshLayout().setRefreshing(true);
                        weatherActivity.requestWeather(weatherId);
                    }
                }
            }

        });

        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLevel == LEVEL_COUNTY) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    queryProvincies();
                }
            }
        });

        queryProvincies();
    }

    private void queryProvincies() {
        tvTitle.setText("中国");
        tvBack.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.isEmpty()) {

            String address = "http://guolin.tech/api/china";
            queryFromServer(address, "province");

        } else {

            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getName());
            }

            adapter.notifyDataSetChanged();
            lvListView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }
    }

    private void queryCities() {
        tvTitle.setText(selectedProvince.getName());
        tvBack.setVisibility(View.VISIBLE);

        cityList = DataSupport.where("provinceId = ?", String.valueOf(selectedProvince.getProvinceCode())).find(City.class);

        if (cityList.isEmpty()) {
            String address = "http://guolin.tech/api/china/" + selectedProvince.getProvinceCode();
            queryFromServer(address, "city");
        } else {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getName());
            }

            adapter.notifyDataSetChanged();
            lvListView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }
    }

    private void queryCounties() {

        tvTitle.setText(selectedCity.getName());
        tvBack.setVisibility(View.VISIBLE);

        countyList = DataSupport.where("cityid = ?", String.valueOf(selectedCity.getCityCode())).find(County.class);

        if (countyList.isEmpty()) {
            String address = "http://guolin.tech/api/china/" + selectedProvince.getProvinceCode() + "/" + selectedCity.getCityCode();
            queryFromServer(address, "county");
        } else {

            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getName());
            }

            adapter.notifyDataSetChanged();
            lvListView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }
    }


    private void queryFromServer(String address, final String type) {

        showProgressDialog();


        HttpUtil.INSTANCE.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String reponseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(reponseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(reponseText, selectedProvince.getProvinceCode());
                } else if ("county".equals(type)) {
                    result = Utility.handleCountyResponse(reponseText, selectedCity.getCityCode());
                }

                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();

                            if ("province".equals(type)) {
                                queryProvincies();
                            } else if ("city".equals(type)) {
                                queryCities();
                            } else if ("county".equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext().getApplicationContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });

    }

    private void showProgressDialog() {

        if (null == progressDialog) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }
}
