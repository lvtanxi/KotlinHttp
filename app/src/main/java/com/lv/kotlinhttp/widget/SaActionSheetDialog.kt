package com.lv.kotlinhttp.widget

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.lv.kotlinhttp.R
import kotlinx.android.synthetic.main.view_actionsheet.view.*


/**
 * Date: 2017-07-17
 * Time: 11:33
 * Description:
 */
class SaActionSheetDialog(private var context: Context, private val scrolle: Int = 6) {
    private var display: DisplayMetrics = DisplayMetrics()
    private var dialog: Dialog? = null
    private lateinit var rootView: View
    private var sheetItemList: MutableList<SheetItem>
    private var hasCreate = false

    init {
        (context as Activity).windowManager.defaultDisplay.getMetrics(display)
        sheetItemList = ArrayList()
    }

    fun builder(): SaActionSheetDialog {
        rootView = LayoutInflater.from(context).inflate(R.layout.view_actionsheet, null)
        rootView.minimumWidth = display.widthPixels
        rootView.txt_cancel.setOnClickListener { dialog?.dismiss() }
        dialog = Dialog(context, R.style.ActionSheetDialogStyle)
        dialog?.setContentView(rootView)
        val dialogWindow = dialog?.window
        dialogWindow?.setGravity(Gravity.START or Gravity.BOTTOM)
        val lp = dialogWindow?.attributes
        lp?.x = 0
        lp?.y = 0
        dialogWindow?.attributes = lp
        return this
    }

    fun setTitle(title: CharSequence): SaActionSheetDialog {
        rootView.stxt_title.visibility = View.VISIBLE
        rootView.stxt_title.text = title
        return this
    }

    fun setCancelable(cancel: Boolean): SaActionSheetDialog {
        dialog?.setCancelable(cancel)
        return this
    }

    fun setCanceledOnTouchOutside(cancel: Boolean): SaActionSheetDialog {
        dialog?.setCanceledOnTouchOutside(cancel)
        return this
    }


    fun addSheetItem(itemName: String, color: SheetItemColor = SheetItemColor.Blue, listener: ((which: Int) -> Unit)? = null): SaActionSheetDialog {
        sheetItemList.add(SheetItem(itemName, color, listener))
        return this
    }

    fun addSheetItems(itemNames: MutableList<String>, listener: ((which: Int) -> Unit)? = null, color: SheetItemColor = SheetItemColor.Blue): SaActionSheetDialog {
        for (itemName in itemNames) {
            addSheetItem(itemName, color, listener)
        }
        return this
    }

    private fun createSheetItems() {
        if (sheetItemList.isEmpty()) return
        val size = sheetItemList.size
        if (size > scrolle) {
            val params = rootView.sLayout_content.layoutParams
            params.height = display.heightPixels / 2
        }
        var sheetItem: SheetItem
        var textView: TextView
        var view: View
        val height = (45 * display.density + 0.5f).toInt()
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        val lineParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1)
        for (i in 0 until size) {
            sheetItem = sheetItemList[i]
            textView = TextView(context)
            textView.text = sheetItem.name
            textView.textSize = 16f
            textView.gravity = Gravity.CENTER
            textView.setBackgroundResource(R.drawable.bg_alertbuttont_item)
            textView.setTextColor(Color.parseColor(sheetItem.color.color))
            textView.layoutParams = params
            textView.setOnClickListener {
                sheetItem.itemClickListener?.invoke(i)
                dialog?.dismiss()
            }
            if (rootView.stxt_title.visibility == View.VISIBLE || i > 0) {
                view = View(context)
                view.setBackgroundResource(R.color.bgColor_divier)
                view.layoutParams = lineParam
                rootView.lLayout_content.addView(view)
            }
            rootView.lLayout_content.addView(textView)
        }
        hasCreate = true
    }

    fun show() {
        if (!hasCreate)
            createSheetItems()
        dialog?.show()
    }


    fun clearSheetItem() {
        hasCreate = false
        sheetItemList.clear()
    }


    enum class SheetItemColor(val color: String) {
        Blue("#037BFF"), Red("#FD4A2E")
    }

    data class SheetItem(
            val name: String,
            val color: SheetItemColor = SheetItemColor.Blue,
            val itemClickListener: ((which: Int) -> Unit)? = null)


}