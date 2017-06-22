package com.lv.kotlinhttp

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

/**
 * Date: 2017-06-22
 * Time: 08:49
 * Description:
 */
class App :Application(){
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
    }
}