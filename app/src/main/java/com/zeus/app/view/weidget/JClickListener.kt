package com.zeus.app.view.weidget

import android.view.View
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class JClickListener(time:Int = 2,clickListener: (View?) -> Unit) : View.OnClickListener {
    private var clickListener: ClickListener? = null
    private var emitter: ObservableEmitter<View?>? = null

    init {

        this.clickListener = object : ClickListener {
            override fun onClick(view: View?) {
                clickListener(view)
            }
        }

        Observable.create<View?> {
            this.emitter = it
        }
            .throttleFirst(time.toLong(), TimeUnit.SECONDS)
            .subscribe(object : Observer<View?> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: View) {
                    (this@JClickListener.clickListener as ClickListener).onClick(t)
                }

                override fun onError(e: Throwable) {
                }

            })
    }

    override fun onClick(p0: View?) {
        emitter?.onNext(p0!!)
    }
}

private interface ClickListener {
    fun onClick(view: View?)
}

