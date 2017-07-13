package com.lv.kotlinhttp.ui

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.alibaba.android.arouter.launcher.ARouter
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection
import com.lv.kotlinhttp.R
import com.lv.kotlinhttp.UpdateDialog
import com.lv.kotlinhttp.model.UpdateBean
import com.lv.kotlinhttp.net.LoadingSubscriber
import com.lv.kotlinhttp.net.RetrofitClient
import com.lv.kotlinhttp.net.WidgetInterface
import com.lv.kotlinhttp.util.DLog
import com.lv.kotlinhttp.util.intoCompositeSubscription
import com.lv.kotlinhttp.util.io_main
import rx.subscriptions.CompositeSubscription
import java.io.File
import java.net.Proxy


class MainActivity : AppCompatActivity(), WidgetInterface {
    private lateinit var ss: ProgressDialog
    private var compositeSubscription: CompositeSubscription? = CompositeSubscription()
    private var updateDialog:UpdateDialog?=null
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
                    success {
                        showUpdateDailog(it)
                    }
                })).intoCompositeSubscription(compositeSubscription)
    }

    fun dARoute(view: View) {
        ARouter.getInstance().build("/test/activity").navigation()
    }


    fun showUpdateDailog(updateBean: UpdateBean) {
        updateDialog= UpdateDialog.Builder(this)
                .setMessage("发现新版本\n1，xxxxxxxx\n2，ooooooooo")
                .setOnUpdateListener {
                    downLoadApp(updateBean)
                }.create()
        updateDialog?.show()
    }

    private fun downLoadApp(updateBean: UpdateBean) {
        FileDownloader.setupOnApplicationOnCreate(application)
                .connectionCreator(FileDownloadUrlConnection.Creator(FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15000) // set connection timeout.
                        .readTimeout(15000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                ))
                .commit()
        FileDownloader.getImpl().create("http://mt.wumart.com/app/down/WmHelper.apk")
                .setPath("${Environment.getExternalStorageDirectory()}${File.separator}WmHelper.apk")
                .setListener(object : FileDownloadListener() {
                    override fun warn(task: BaseDownloadTask?) {
                    }

                    override fun completed(task: BaseDownloadTask?) {
                        toastMessage("completed")
                        insterApp()
                    }

                    override fun pending(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    }

                    override fun error(task: BaseDownloadTask?, e: Throwable?) {
                        DLog.d(e)
                    }

                    override fun paused(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                    }

                    override fun progress(task: BaseDownloadTask?, soFarBytes: Int, totalBytes: Int) {
                        updateDialog?.setNumberProgress(soFarBytes * 100 / totalBytes)
                    }
                }).start()
    }

    private fun insterApp() {
        updateDialog?.dismiss()
        //安装文件apk路径
        val fileName ="${Environment.getExternalStorageDirectory()}${File.separator}WmHelper.apk"
        //创建URI
        val uri = Uri.fromFile(File(fileName))
        //创建Intent意图
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK//启动新的activity
        //设置Uri和类型
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        //执行安装
        startActivity(intent)

        finish()
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
