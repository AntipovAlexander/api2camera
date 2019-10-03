package com.antipov.camera2apiidp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.antipov.camera2apiidp.R
import com.antipov.camera2apiidp.callbacks.OrientationChangeCallback
import kotlinx.android.synthetic.main.fragment_camera.*

class CameraFragment : Fragment() {

    private lateinit var cameraCaptureSessions: CameraCaptureSession
    private lateinit var captureRequestBuilder: CaptureRequest.Builder
    private lateinit var cameraDevice: CameraDevice
    private lateinit var imageDimension: Size
    private lateinit var orientetionChangeCallback: OrientationChangeCallback
    private var cameraId: String = "0"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = View.inflate(inflater.context, R.layout.fragment_camera, null)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orientetionChangeCallback = OrientationChangeCallback(activity!!)
        orientetionChangeCallback.enable()
    }

    override fun onResume() {
        super.onResume()
        // set listener about surface availability
        texture.surfaceTextureListener = surfaceTextureListener
    }

    override fun onPause() {
        super.onPause()
        // remove listener listener about surface availability
        texture.surfaceTextureListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        orientetionChangeCallback.disable()
    }

    private val surfaceTextureListener = object : TextureView.SurfaceTextureListener {

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
        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            openCamera(width, height)
        }
    }

    private val cameraStateCallback = object : CameraDevice.StateCallback() {

        /**
         * The method called when a camera device has finished opening.
         *
         * Camera device is ready now. Can start CaptureSession at this moment.
         */
        override fun onOpened(camera: CameraDevice) {
            // save to close it later
            cameraDevice = camera
            // create CaptureSession
            createCameraPreview()
        }

        /**
         * The method called when a camera device is no longer available for use.
         */
        override fun onDisconnected(camera: CameraDevice) {
            cameraDevice = camera
            cameraDevice.close()
        }

        /**
         * The method called when a camera device has encountered a serious error.
         */
        override fun onError(camera: CameraDevice, p1: Int) {
            cameraDevice = camera
            cameraDevice.close()
        }

        /**
         * The method called when a camera device has been closed with CameraDevice#close.
         */
        override fun onClosed(camera: CameraDevice) {
            cameraDevice = camera
        }
    }

    private fun createCameraPreview() {
        // get surface texture for output
        val surfaceTexture = texture.surfaceTexture
        // configure it with selected size
        surfaceTexture.setDefaultBufferSize(imageDimension.width, imageDimension.height)
        // create surface
        val surface = Surface(surfaceTexture)
        // make request for preview
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        // add surface as target
        captureRequestBuilder.addTarget(surface)
        cameraDevice.createCaptureSession(
            listOf(surface),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession
                    // and set this reques
                    updatePreview()
                }

                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {

                }
            },
            null
        )
    }

    private fun updatePreview() {
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO)
        //With this method, the camera device will continually capture images, cycling through the
        // settings in the provided list of CaptureRequests, at the maximum rate possible.
        cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, null)
    }

    @SuppressLint("MissingPermission")
    private fun openCamera(width: Int, height: Int) {
        // check permissions
        val permission = ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            activity!!.finish()
            return
        }
//        setUpCameraOutputs(width, height)
//        configureTransform(width, height)
        try {
            // get camera possible resolutions
            val manager = activity!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            val characteristics = manager.getCameraCharacteristics(cameraId)
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)!!
            // this method returns array of Sizes, compatible with class to use as an output
            // just pick first one. There you can choose appropriate for you size
            imageDimension = map.getOutputSizes(SurfaceTexture::class.java)[0]

            // We fit the aspect ratio of TextureView to the size of preview we picked.
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                texture.setAspectRatio(imageDimension.width, imageDimension.height)
            } else {
                texture.setAspectRatio(imageDimension.height, imageDimension.width)
            }

            // open camera with state callback
            manager.openCamera(cameraId, cameraStateCallback, null)
        } catch (e: CameraAccessException) {
            Log.e(TAG, e.toString())
        } catch (e: InterruptedException) {
            throw RuntimeException("Interrupted while trying to lock camera opening.", e)
        }

    }

}