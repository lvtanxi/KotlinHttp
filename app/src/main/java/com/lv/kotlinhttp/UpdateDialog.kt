package com.lv.kotlinhttp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.lib_update_app_dialog.*


/**
 * Date: 2017-07-13
 * Time: 15:33
 * Description:
 */
class UpdateDialog(private val builder: Builder) : Dialog(builder.context, R.style.Update_Dialog) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.setPadding(0, 0, 0, 0)
        //设置窗口宽度为充满全屏
        window.attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        //设置窗口高度为包裹内容
        window.attributes.height = WindowManager.LayoutParams.MATCH_PARENT
        setContentView(R.layout.lib_update_app_dialog)
        tv_update_info.text = builder.message
        setCancelable(builder.cancelable)
        setCanceledOnTouchOutside(builder.canceledOnTouchOutside)
        ll_close.visibility = if (builder.hideClose) View.GONE else View.VISIBLE
        btn_ok.setOnClickListener {
            btn_ok.visibility = View.GONE
            npb.visibility = View.VISIBLE
            builder.updateListener?.invoke()
        }
        if (!builder.hideClose)
            ll_close.setOnClickListener { dismiss() }
    }

    fun setNumberProgress(progress: Int) {
        npb.progress = progress
    }


    class Builder(val context: Context) {
        var message: CharSequence = ""
        var cancelable = false
        var hideClose = false
        var canceledOnTouchOutside = true
        var updateListener: (() -> Unit)? = null

        fun setMessage(message: CharSequence): Builder {
            this.message = message
            return this
        }

        fun setCancelable(flag: Boolean): Builder {
            this.cancelable = flag
            return this
        }

        fun setHideClose(flag: Boolean): Builder {
            this.hideClose = flag
            return this
        }

        fun setCanceledOnTouchOutside(cancel: Boolean): Builder {
            this.canceledOnTouchOutside = cancel
            return this
        }

        fun setOnUpdateListener(listener: () -> Unit): Builder {
            this.updateListener = listener
            return this
        }

        fun create() = UpdateDialog(this)
    }


}