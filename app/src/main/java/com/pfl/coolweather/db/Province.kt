package com.pfl.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by Administrator on 2017/10/6 0006.
 */

class Province : DataSupport() {

    var id: Int = 0
    var provinceCode: Int = 0
    var name: String? = null
}
