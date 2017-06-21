package com.lv.kotlinhttp.util

import com.lv.kotlinhttp.BuildConfig
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * Date: 2017-06-21
 * Time: 14:55
 * Description:
 */
object DLog {

    fun init(){
        Logger.addLogAdapter(AndroidLogAdapter())
    }

    fun d(any: Any?) {
        if (BuildConfig.DEBUG) {
            val message = any ?: "打印了空消息"
            Logger.d(message.toString())
        }

    }
}