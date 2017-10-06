package com.pfl.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by Administrator on 2017/10/6 0006.
 */

class County : DataSupport() {

    var id: Int = 0
    var contyCode: Int = 0
    var name: String? = null
    var weatherId: String? = null
    var cityId: Int = 0
}
