package com.antipov.camera2apiidp.view

import android.view.animation.AlphaAnimation
import android.view.animation.LinearInterpolator

class BlinkAnimation : AlphaAnimation(1f, 0f) {
    init {
        duration = 500
        interpolator = LinearInterpolator()
        fillAfter = true
    }
}