package com.lv.kotlinhttp.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.lv.kotlinhttp.R
import com.lv.kotlinhttp.model.UpdateBean
import com.lv.kotlinhttp.net.LoadingSubscriber
import com.lv.kotlinhttp.net.RetrofitClient
import com.lv.kotlinhttp.net.WidgetInterface
import com.lv.kotlinhttp.util.DLog
import com.lv.kotlinhttp.util.io_main
import rx.Subscription
import rx.subscriptions.CompositeSubscription

class MainActivity : AppCompatActivity(), WidgetInterface {
    private lateinit var ss: ProgressDialog
    private var compositeSubscription: CompositeSubscription? = null
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
        addSubscription(RetrofitClient
                .getInstance()
                .apiInterface
                .getNewest()
                .io_main()
                .subscribe(LoadingSubscriber<UpdateBean>(this, {
                    success { }
                })))
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

    fun addSubscription(subscription: Subscription) {
        if (compositeSubscription == null)
            compositeSubscription = CompositeSubscription()
        compositeSubscription?.add(subscription)
    }

    override fun onDestroy() {
        compositeSubscription?.unsubscribe()
        compositeSubscription = null
        super.onDestroy()
    }

}
