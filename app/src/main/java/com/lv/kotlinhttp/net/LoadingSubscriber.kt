package com.lv.kotlinhttp.net

import rx.Subscriber
import java.lang.ref.WeakReference
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * Date: 2017-06-21
 * Time: 15:50
 * Description:
 */
open class LoadingSubscriber<T>(widgetInterface: WidgetInterface, listener: SimpleCallBack<T>.() -> Unit, val showLoadingView: Boolean = true,
                                val toastMessgae: Boolean = true) : Subscriber<T>() {

    protected val weakReference: WeakReference<WidgetInterface> = WeakReference(widgetInterface)
    protected val callBack: SimpleCallBack<T> = bindSimpleCallBack(listener)


    override fun onStart() {
        if (showLoadingView)
            weakReference.get()?.showLoadingView()
    }

    override fun onNext(t: T) {
        callBack.onSuccess(t)
    }

    override fun onError(e: Throwable?) {
        var code = 0
        if (toastMessgae) {
            e?.let {
                val error: String?
                if (e is ConnectException) {
                    error = "服务器连接错误，请稍后重试。"
                    code = 1
                } else if (e is SocketTimeoutException) {
                    error = "服务器连接超时，请稍后重试。"
                    code = 2
                } else if (e is HttpException) {
                    code = e.code
                    error = e.message
                } else {
                    error = e.message
                }
                weakReference.get()?.toastMessage(error)
            }
        }
        if (showLoadingView)
            weakReference.get()?.hideLoadingView()
        callBack.onFailure(code)
        callBack.onFinish()
    }

    override fun onCompleted() {
        if (showLoadingView)
            weakReference.get()?.hideLoadingView()
        callBack.onFinish()
    }

    fun bindSimpleCallBack(listener: SimpleCallBack<T>.() -> Unit): SimpleCallBack<T> {
        val ca = SimpleCallBack<T>()
        ca.listener()
        return ca
    }
}
