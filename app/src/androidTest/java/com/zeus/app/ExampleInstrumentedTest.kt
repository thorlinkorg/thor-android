package com.zeus.app

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.zeus.app", appContext.packageName)
    }


    @Test
    fun testss(){
        Observable.create<String> {
            Log.d("1111", "testss: ")
            Thread.sleep(3000)
            it.onNext("1111111111111111111111111111111")
        }.subscribe(object : Observer<String> {
            override fun onComplete() {
                Log.d("111", "onComplete: ")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d("111", "onSubscribe: ")
            }

            override fun onNext(t: String) {
                Log.d("111", "onNext: ")
            }

            override fun onError(e: Throwable) {
                Log.d("111", "onError: ")
            }

        })
    }
}