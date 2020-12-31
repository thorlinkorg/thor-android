package com.zeus.app.utils

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View

var objectAnimator: ObjectAnimator? = null
fun rotation360(view: View,time:Long = 2000) {
    val objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
    objectAnimator.duration = time
    objectAnimator.repeatCount = ValueAnimator.INFINITE
    objectAnimator.repeatMode = ObjectAnimator.RESTART//匀速
    objectAnimator.start()//开始（重新开始）
}

fun stop(){
    objectAnimator?.end()
}