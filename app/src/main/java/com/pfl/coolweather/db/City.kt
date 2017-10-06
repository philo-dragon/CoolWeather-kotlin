package com.pfl.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by Administrator on 2017/10/6 0006.
 */

class City : DataSupport() {

    var id: Int = 0
    var name: String? = null
    var cityCode: Int = 0
    var provinceId: Int = 0
}
