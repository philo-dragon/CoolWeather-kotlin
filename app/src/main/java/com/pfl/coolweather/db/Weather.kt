package com.pfl.coolweather.db

/**
 * Created by Administrator on 2017/10/7 0007.
 */

class Weather {


    /**
     * aqi : {"city":{"aqi":"155","co":"1","no2":"75","o3":"8","pm10":"144","pm25":"118","qlty":"中度污染","so2":"3"}}
     * basic : {"city":"北京","cnty":"中国","id":"CN101010100","lat":"39.90498734","lon":"116.40528870","update":{"loc":"2017-10-07 11:46","utc":"2017-10-07 03:46"}}
     * daily_forecast : [{"astro":{"mr":"19:03","ms":"07:32","sr":"06:16","ss":"17:48"},"cond":{"code_d":"305","code_n":"104","txt_d":"小雨","txt_n":"阴"},"date":"2017-10-07","hum":"65","pcpn":"0.0","pop":"0","pres":"1019","tmp":{"max":"19","min":"13"},"uv":"2","vis":"19","wind":{"deg":"62","dir":"东北风","sc":"微风","spd":"5"}},{"astro":{"mr":"19:43","ms":"08:41","sr":"06:17","ss":"17:46"},"cond":{"code_d":"305","code_n":"305","txt_d":"小雨","txt_n":"小雨"},"date":"2017-10-08","hum":"76","pcpn":"0.0","pop":"58","pres":"1021","tmp":{"max":"19","min":"13"},"uv":"2","vis":"19","wind":{"deg":"0","dir":"北风","sc":"微风","spd":"5"}},{"astro":{"mr":"20:27","ms":"09:51","sr":"06:18","ss":"17:45"},"cond":{"code_d":"306","code_n":"305","txt_d":"中雨","txt_n":"小雨"},"date":"2017-10-09","hum":"69","pcpn":"36.1","pop":"82","pres":"1023","tmp":{"max":"15","min":"9"},"uv":"1","vis":"14","wind":{"deg":"350","dir":"北风","sc":"微风","spd":"9"}}]
     * hourly_forecast : [{"cond":{"code":"103","txt":"晴间多云"},"date":"2017-10-07 13:00","hum":"59","pop":"52","pres":"1018","tmp":"19","wind":{"deg":"137","dir":"东南风","sc":"微风","spd":"6"}},{"cond":{"code":"101","txt":"多云"},"date":"2017-10-07 16:00","hum":"74","pop":"65","pres":"1018","tmp":"18","wind":{"deg":"132","dir":"东南风","sc":"微风","spd":"4"}},{"cond":{"code":"103","txt":"晴间多云"},"date":"2017-10-07 19:00","hum":"83","pop":"24","pres":"1020","tmp":"16","wind":{"deg":"279","dir":"西风","sc":"微风","spd":"3"}},{"cond":{"code":"103","txt":"晴间多云"},"date":"2017-10-07 22:00","hum":"81","pop":"19","pres":"1021","tmp":"15","wind":{"deg":"125","dir":"东南风","sc":"微风","spd":"4"}}]
     * now : {"cond":{"code":"104","txt":"阴"},"fl":"16","hum":"77","pcpn":"0","pres":"1020","tmp":"16","vis":"7","wind":{"deg":"179","dir":"南风","sc":"微风","spd":"6"}}
     * status : ok
     * suggestion : {"air":{"brf":"良","txt":"气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。"},"comf":{"brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"},"cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},"drsg":{"brf":"较舒适","txt":"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"},"flu":{"brf":"较易发","txt":"天气转凉，空气湿度较大，较易发生感冒，体质较弱的朋友请注意适当防护。"},"sport":{"brf":"较不宜","txt":"有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意携带雨具并注意避雨防滑。"},"trav":{"brf":"适宜","txt":"温度适宜，又有较弱降水和微风作伴，会给您的旅行带来意想不到的景象，适宜旅游，可不要错过机会呦！"},"uv":{"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}}
     */

    var aqi: AqiBean? = null
    var basic: BasicBean? = null
    var now: NowBean? = null
    var status: String? = null
    var suggestion: SuggestionBean? = null
    var daily_forecast: List<DailyForecastBean>? = null
    var hourly_forecast: List<HourlyForecastBean>? = null

    class AqiBean {
        /**
         * city : {"aqi":"155","co":"1","no2":"75","o3":"8","pm10":"144","pm25":"118","qlty":"中度污染","so2":"3"}
         */

        var city: CityBean? = null

        class CityBean {
            /**
             * aqi : 155
             * co : 1
             * no2 : 75
             * o3 : 8
             * pm10 : 144
             * pm25 : 118
             * qlty : 中度污染
             * so2 : 3
             */

            var aqi: String? = null
            var co: String? = null
            var no2: String? = null
            var o3: String? = null
            var pm10: String? = null
            var pm25: String? = null
            var qlty: String? = null
            var so2: String? = null
        }
    }

    class BasicBean {
        /**
         * city : 北京
         * cnty : 中国
         * id : CN101010100
         * lat : 39.90498734
         * lon : 116.40528870
         * update : {"loc":"2017-10-07 11:46","utc":"2017-10-07 03:46"}
         */

        var city: String? = null
        var cnty: String? = null
        var id: String? = null
        var lat: String? = null
        var lon: String? = null
        var update: UpdateBean? = null

        class UpdateBean {
            /**
             * loc : 2017-10-07 11:46
             * utc : 2017-10-07 03:46
             */

            var loc: String? = null
            var utc: String? = null
        }
    }

    class NowBean {
        /**
         * cond : {"code":"104","txt":"阴"}
         * fl : 16
         * hum : 77
         * pcpn : 0
         * pres : 1020
         * tmp : 16
         * vis : 7
         * wind : {"deg":"179","dir":"南风","sc":"微风","spd":"6"}
         */

        var cond: CondBean? = null
        var fl: String? = null
        var hum: String? = null
        var pcpn: String? = null
        var pres: String? = null
        var tmp: String? = null
        var vis: String? = null
        var wind: WindBean? = null

        class CondBean {
            /**
             * code : 104
             * txt : 阴
             */

            var code: String? = null
            var txt: String? = null
        }

        class WindBean {
            /**
             * deg : 179
             * dir : 南风
             * sc : 微风
             * spd : 6
             */

            var deg: String? = null
            var dir: String? = null
            var sc: String? = null
            var spd: String? = null
        }
    }

    class SuggestionBean {
        /**
         * air : {"brf":"良","txt":"气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。"}
         * comf : {"brf":"舒适","txt":"白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。"}
         * cw : {"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"}
         * drsg : {"brf":"较舒适","txt":"建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。"}
         * flu : {"brf":"较易发","txt":"天气转凉，空气湿度较大，较易发生感冒，体质较弱的朋友请注意适当防护。"}
         * sport : {"brf":"较不宜","txt":"有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意携带雨具并注意避雨防滑。"}
         * trav : {"brf":"适宜","txt":"温度适宜，又有较弱降水和微风作伴，会给您的旅行带来意想不到的景象，适宜旅游，可不要错过机会呦！"}
         * uv : {"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}
         */

        var air: AirBean? = null
        var comf: ComfBean? = null
        var cw: CwBean? = null
        var drsg: DrsgBean? = null
        var flu: FluBean? = null
        var sport: SportBean? = null
        var trav: TravBean? = null
        var uv: UvBean? = null

        class AirBean {
            /**
             * brf : 良
             * txt : 气象条件有利于空气污染物稀释、扩散和清除，可在室外正常活动。
             */

            var brf: String? = null
            var txt: String? = null
        }

        class ComfBean {
            /**
             * brf : 舒适
             * txt : 白天不太热也不太冷，风力不大，相信您在这样的天气条件下，应会感到比较清爽和舒适。
             */

            var brf: String? = null
            var txt: String? = null
        }

        class CwBean {
            /**
             * brf : 不宜
             * txt : 不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。
             */

            var brf: String? = null
            var txt: String? = null
        }

        class DrsgBean {
            /**
             * brf : 较舒适
             * txt : 建议着薄外套、开衫牛仔衫裤等服装。年老体弱者应适当添加衣物，宜着夹克衫、薄毛衣等。
             */

            var brf: String? = null
            var txt: String? = null
        }

        class FluBean {
            /**
             * brf : 较易发
             * txt : 天气转凉，空气湿度较大，较易发生感冒，体质较弱的朋友请注意适当防护。
             */

            var brf: String? = null
            var txt: String? = null
        }

        class SportBean {
            /**
             * brf : 较不宜
             * txt : 有降水，推荐您在室内进行健身休闲运动；若坚持户外运动，须注意携带雨具并注意避雨防滑。
             */

            var brf: String? = null
            var txt: String? = null
        }

        class TravBean {
            /**
             * brf : 适宜
             * txt : 温度适宜，又有较弱降水和微风作伴，会给您的旅行带来意想不到的景象，适宜旅游，可不要错过机会呦！
             */

            var brf: String? = null
            var txt: String? = null
        }

        class UvBean {
            /**
             * brf : 最弱
             * txt : 属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。
             */

            var brf: String? = null
            var txt: String? = null
        }
    }

    class DailyForecastBean {
        /**
         * astro : {"mr":"19:03","ms":"07:32","sr":"06:16","ss":"17:48"}
         * cond : {"code_d":"305","code_n":"104","txt_d":"小雨","txt_n":"阴"}
         * date : 2017-10-07
         * hum : 65
         * pcpn : 0.0
         * pop : 0
         * pres : 1019
         * tmp : {"max":"19","min":"13"}
         * uv : 2
         * vis : 19
         * wind : {"deg":"62","dir":"东北风","sc":"微风","spd":"5"}
         */

        var astro: AstroBean? = null
        var cond: CondBeanX? = null
        var date: String? = null
        var hum: String? = null
        var pcpn: String? = null
        var pop: String? = null
        var pres: String? = null
        var tmp: TmpBean? = null
        var uv: String? = null
        var vis: String? = null
        var wind: WindBeanX? = null

        class AstroBean {
            /**
             * mr : 19:03
             * ms : 07:32
             * sr : 06:16
             * ss : 17:48
             */

            var mr: String? = null
            var ms: String? = null
            var sr: String? = null
            var ss: String? = null
        }

        class CondBeanX {
            /**
             * code_d : 305
             * code_n : 104
             * txt_d : 小雨
             * txt_n : 阴
             */

            var code_d: String? = null
            var code_n: String? = null
            var txt_d: String? = null
            var txt_n: String? = null
        }

        class TmpBean {
            /**
             * max : 19
             * min : 13
             */

            var max: String? = null
            var min: String? = null
        }

        class WindBeanX {
            /**
             * deg : 62
             * dir : 东北风
             * sc : 微风
             * spd : 5
             */

            var deg: String? = null
            var dir: String? = null
            var sc: String? = null
            var spd: String? = null
        }
    }

    class HourlyForecastBean {
        /**
         * cond : {"code":"103","txt":"晴间多云"}
         * date : 2017-10-07 13:00
         * hum : 59
         * pop : 52
         * pres : 1018
         * tmp : 19
         * wind : {"deg":"137","dir":"东南风","sc":"微风","spd":"6"}
         */

        var cond: CondBeanXX? = null
        var date: String? = null
        var hum: String? = null
        var pop: String? = null
        var pres: String? = null
        var tmp: String? = null
        var wind: WindBeanXX? = null

        class CondBeanXX {
            /**
             * code : 103
             * txt : 晴间多云
             */

            var code: String? = null
            var txt: String? = null
        }

        class WindBeanXX {
            /**
             * deg : 137
             * dir : 东南风
             * sc : 微风
             * spd : 6
             */

            var deg: String? = null
            var dir: String? = null
            var sc: String? = null
            var spd: String? = null
        }
    }
}
