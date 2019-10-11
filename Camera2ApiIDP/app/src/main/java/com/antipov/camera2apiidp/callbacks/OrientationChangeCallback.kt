package com.antipov.camera2apiidp.callbacks

import android.animation.ObjectAnimator
import android.content.Context
import android.view.OrientationEventListener
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.antipov.camera2apiidp.R
import com.antipov.camera2apiidp.utils.Rotation

class OrientationChangeCallback(private val context: Context) : OrientationEventListener(context) {

    private val takePhotoBtn
        get() = (context as? AppCompatActivity)?.findViewById<View>(R.id.takePhotoBtn)

    private val imagePreview
        get() = (context as? AppCompatActivity)?.findViewById<View>(R.id.imagePreview)

    private var rotation = Rotation.ROTATION_O.angle

    override fun onOrientationChanged(orientation: Int) {

        when {
            (orientation >= 340 || orientation < 20) and (rotation != Rotation.ROTATION_O.angle) -> {
                val direction = determineDirection(rotation, Rotation.ROTATION_180.angle)
                rotateViewsTo(Rotation.ROTATION_O.angle * direction, takePhotoBtn, imagePreview)
                rotation = Rotation.ROTATION_O.angle
            }
            (orientation in 70..109) and (rotation != Rotation.ROTATION_90.angle) -> {
                val direction = determineDirection(rotation, Rotation.ROTATION_90.angle)
                rotateViewsTo(-Rotation.ROTATION_90.angle * direction, takePhotoBtn, imagePreview)
                rotation = Rotation.ROTATION_90.angle
            }
            (orientation in 250..289) and (rotation != Rotation.ROTATION_270.angle) -> {
                val direction = determineDirection(rotation, Rotation.ROTATION_270.angle)
                rotateViewsTo(Rotation.ROTATION_90.angle * direction, takePhotoBtn, imagePreview)
                rotation = Rotation.ROTATION_270.angle
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