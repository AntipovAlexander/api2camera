package com.antipov.camera2apiidp.callbacks

import android.graphics.SurfaceTexture
import android.view.TextureView

class PreviewSurfaceTextureListener(private val callback: () -> Unit) :
    TextureView.SurfaceTextureListener {

    /**
     * Invoked when the SurfaceTexture's buffers size changed.
     */
    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) = Unit

    /**
     * Invoked when the specified SurfaceTexture is updated through SurfaceTexture#updateTexImage().
     */
    override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) = Unit

    /**
     * Invoked when the specified SurfaceTexture is about to be destroyed.
     *
     * f returns true, no rendering should happen inside the surface texture after this method is invoked.
     * If returns false, the client needs to call SurfaceTexture#release(). Most applications should return true.
     */
    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?) = true

    /**
     * Invoked when a TextureView's SurfaceTexture is ready for use.
     */
    override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) =
        callback.invoke()
}