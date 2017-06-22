package com.lv.kotlinhttp.util

import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Date: 2017-06-21
 * Time: 17:08
 * Description:
 */
fun <T> Observable<T>.io_main(): Observable<T> {
    return this.compose({ tObservable -> tObservable.subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()) })
}

fun Subscription.intoCompositeSubscription(compositeSubscription: CompositeSubscription?) {
    compositeSubscription?.add(this)
}