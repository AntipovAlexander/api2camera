package com.antipov.camera2apiidp.callbacks

import android.animation.ObjectAnimator
import android.content.Context
import android.view.OrientationEventListener
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.antipov.camera2apiidp.R

class OrientationChangeCallback(private val context: Context) : OrientationEventListener(context) {

    private val takePhotoBtn
        get() = (context as? AppCompatActivity)?.findViewById<ImageButton>(R.id.takePhotoBtn)

    private val ROTATION_O = 0
    private val ROTATION_90 = 90
    private val ROTATION_180 = 180
    private val ROTATION_270 = 270
    private var rotation = ROTATION_O

    override fun onOrientationChanged(orientation: Int) {

        when {
            (orientation >= 340 || orientation < 20) and (rotation != ROTATION_O) -> {
                val direction = determineDirection(rotation, ROTATION_180)
                rotateViewsTo(ROTATION_O * direction, takePhotoBtn)
                rotation = ROTATION_O
            }
            (orientation in 70..109) and (rotation != ROTATION_90) -> {
                val direction = determineDirection(rotation, ROTATION_90)
                rotateViewsTo(-ROTATION_90 * direction, takePhotoBtn)
                rotation = ROTATION_90
            }
            (orientation in 250..289) and (rotation != ROTATION_270) -> {
                val direction = determineDirection(rotation, ROTATION_270)
                rotateViewsTo(ROTATION_90 * direction, takePhotoBtn)
                rotation = ROTATION_270
            }
        }
    }

    private fun determineDirection(last: Int, current: Int) = if (last > current) -1 else 1

    private fun rotateViewsTo(newRotation: Int, vararg views: View?) {
        views.filterNotNull().forEach { view ->
            ObjectAnimator
                .ofFloat(view, View.ROTATION, view.rotation, newRotation.toFloat())
                .setDuration(300)
                .start()
        }
    }
}