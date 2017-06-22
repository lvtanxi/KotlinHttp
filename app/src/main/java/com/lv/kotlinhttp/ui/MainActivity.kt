package com.lv.kotlinhttp.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.lv.kotlinhttp.R
import com.lv.kotlinhttp.model.UpdateBean
import com.lv.kotlinhttp.net.LoadingSubscriber
import com.lv.kotlinhttp.net.RetrofitClient
import com.lv.kotlinhttp.net.WidgetInterface
import com.lv.kotlinhttp.util.DLog
import com.lv.kotlinhttp.util.intoCompositeSubscription
import com.lv.kotlinhttp.util.io_main
import rx.subscriptions.CompositeSubscription

class MainActivity : AppCompatActivity(), WidgetInterface {
    private lateinit var ss: ProgressDialog
    private var compositeSubscription: CompositeSubscription? = CompositeSubscription()
    var count = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ss = ProgressDialog(this)
        DLog.init()
    }

    fun dlog(view: View) {
        DLog.d(if (count % 2 == 0) null else "asdfasdf")
        count++
    }

    fun dhttp(view: View) {
        RetrofitClient
                .getInstance()
                .apiInterface
                .getNewest()
                .io_main()
                .subscribe(LoadingSubscriber<UpdateBean>(this, {
                    success { }
                })).intoCompositeSubscription(compositeSubscription)
    }
    fun dARoute(view: View) {
        ARouter.getInstance().build("/test/activity").navigation()
    }

    override fun showLoadingView() {
        ss.show()
    }

    override fun hideLoadingView() {
        ss.dismiss()
    }

    override fun toastMessage(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        compositeSubscription?.unsubscribe()
        compositeSubscription = null
        super.onDestroy()
    }

}
