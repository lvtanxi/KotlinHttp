package com.lv.kotlinhttp.net

/**
 * Date: 2017-06-21
 * Time: 16:29
 * Description:
 */
open class SimpleCallBack<T> {
    private var _OnSucess: ((t: T) -> Unit)? = null
    fun success(listener: (t: T) -> Unit) {
        _OnSucess = listener
    }

    fun onSuccess(t: T) = _OnSucess?.invoke(t)

    private var _OnFailure: ((code: Int) -> Unit)? = null

    fun onFailure(code: Int) = _OnFailure?.invoke(code)

    fun failure(listener: (code: Int) -> Unit) {
        _OnFailure = listener
    }

    private var _onFinish: (() -> Unit)? = null

   fun onFinish() = _onFinish?.invoke()

    fun finish(listener: () -> Unit) {
        _onFinish = listener
    }

}