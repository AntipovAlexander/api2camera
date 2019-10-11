package com.antipov.camera2apiidp.callbacks

import android.hardware.camera2.CameraDevice

class DeviceStateCallback(
    private val opened: (CameraDevice) -> Unit,
    private val disconnected: (CameraDevice) -> Unit,
    private val error: (CameraDevice) -> Unit,
    private val closed: (CameraDevice) -> Unit
) : CameraDevice.StateCallback() {

    /**
     * The method called when a camera device has finished opening.
     *
     * Camera device is ready now. Can start CaptureSession at this moment.
     */
    override fun onOpened(camera: CameraDevice) = opened(camera)

    /**
     * The method called when a camera device is no longer available for use.
     */
    override fun onDisconnected(camera: CameraDevice) = disconnected(camera)

    /**
     * The method called when a camera device has encountered a serious error.
     */
    override fun onError(camera: CameraDevice, p1: Int) = error(camera)

    /**
     * The method called when a camera device has been closed with CameraDevice#close.
     */
    override fun onClosed(camera: CameraDevice) = closed(camera)
}