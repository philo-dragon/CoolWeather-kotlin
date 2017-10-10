package com.pfl.coolweather

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.pfl.coolweather.ui.activity.WeatherActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var prefs = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
        if (prefs.getString("weather", null) != null) {
            val intent = Intent(this@MainActivity, WeatherActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
