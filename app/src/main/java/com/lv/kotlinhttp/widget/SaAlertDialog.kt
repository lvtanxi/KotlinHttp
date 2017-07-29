package com.lv.kotlinhttp.widget

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.lv.kotlinhttp.R
import com.lv.kotlinhttp.util.setVisibility
import kotlinx.android.synthetic.main.view_alertdialog.view.*

/**
 * Date: 2017-07-17
 * Time: 11:00
 * Description:
 */
class SaAlertDialog(private val context: Context) {
    private var dialog: Dialog? = null
    private val widthPixels: Int
    private var showTitle = false
    private var showMsg = false
    private var showPosBtn = false
    private var showNegBtn = false
    private lateinit var rootView: View
    private var hasCreate = false

    init {
        val display: DisplayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(display)
        widthPixels = (display.widthPixels * 0.85).toInt()
    }

    fun builder(): SaAlertDialog {
        rootView = LayoutInflater.from(context).inflate(R.layout.view_alertdialog, null)
        rootView.txt_title.visibility = View.GONE
        rootView.txt_msg.visibility = View.GONE
        rootView.btn_neg.visibility = View.GONE
        rootView.btn_pos.visibility = View.GONE
        rootView.img_line.visibility = View.GONE
        dialog = Dialog(context, R.style.AlertDialogStyle)
        dialog?.setContentView(rootView)
        rootView.lLayout_bg.layoutParams = FrameLayout.LayoutParams(widthPixels, LinearLayout.LayoutParams.WRAP_CONTENT)
        return this
    }

    fun setTitle(title: CharSequence): SaAlertDialog {
        showTitle = true
        rootView.txt_title.text = title
        return this
    }

    fun setMsg(msg: CharSequence): SaAlertDialog {
        showMsg = true
        rootView.txt_msg.text = msg
        return this
    }

    fun setCancelable(cancel: Boolean): SaAlertDialog {
        dialog?.setCancelable(cancel)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): SaAlertDialog {
        dialog?.setCanceledOnTouchOutside(cancel)
        return this
    }

    fun setPositiveButton(text: CharSequence, listener: ((view: View) -> Unit)? = null): SaAlertDialog {
        showPosBtn = true
        rootView.btn_pos.text = if ("" == text) "确定" else text
        rootView.btn_pos.setOnClickListener {
            listener?.invoke(it)
            dialog?.dismiss()
        }
        return this
    }

    fun setNegativeButton(text: CharSequence, listener: ((view: View) -> Unit)? = null): SaAlertDialog {
        showNegBtn = true
        rootView.btn_neg.text = if ("" == text) "取消" else text
        rootView.btn_neg.setOnClickListener {
            listener?.invoke(it)
            dialog?.dismiss()
        }
        return this
    }

    private fun createLayout() {
        if (!showTitle && !showMsg) {
            rootView.txt_title.text = "提示"
            rootView.txt_title.visibility = View.VISIBLE
        }

        rootView.txt_title.setVisibility(showTitle)
        rootView.txt_msg.setVisibility(showMsg)


        if (!showPosBtn && !showNegBtn) {
            rootView.btn_pos.text = "确定"
            rootView.btn_pos.visibility = View.VISIBLE
            rootView.btn_pos.setBackgroundResource(R.drawable.bg_alertbutton_bottom)
            rootView.btn_pos.setOnClickListener { dialog?.dismiss() }
        }

        if (showPosBtn && showNegBtn) {
            rootView.btn_pos.visibility = View.VISIBLE
            rootView.btn_pos.setBackgroundResource(R.drawable.bg_alertbutton_right)
            rootView.btn_neg.visibility = View.VISIBLE
            rootView.btn_neg.setBackgroundResource(R.drawable.bg_alertbutton_left)
            rootView.img_line.visibility = View.VISIBLE
        }

        if (showPosBtn && !showNegBtn) {
            rootView.btn_pos.visibility = View.VISIBLE
            rootView.btn_pos.setBackgroundResource(R.drawable.bg_alertbutton_bottom)
        }

        if (!showPosBtn && showNegBtn) {
            rootView.btn_neg.visibility = View.VISIBLE
            rootView.btn_neg.setBackgroundResource(R.drawable.bg_alertbutton_bottom)
        }
        hasCreate = true
    }

    fun show(palyAnim: Boolean = true) {
        if (!hasCreate)
            createLayout()
        dialog?.show()
        if (!palyAnim) return
        val scaleX = ObjectAnimator.ofFloat(rootView, "scaleX", 0.3f, 1.05f, 0.9f, 1f)
        val scaleY = ObjectAnimator.ofFloat(rootView, "scaleY", 0.3f, 1.05f, 0.9f, 1f)
        val animatorSet = AnimatorSet()
        animatorSet.duration = 500
        animatorSet.interpolator = BounceInterpolator()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.start()
    }


}