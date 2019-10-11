package com.antipov.camera2apiidp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.antipov.camera2apiidp.R
import com.antipov.camera2apiidp.fragments.CameraFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var permissionsRequest = 667

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                permissionsRequest
            )
        else
            runCamera(savedInstanceState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionsRequest && grantResults.size == 1) runCamera(null)
    }


    private fun runCamera(savedInstanceState: Bundle?) {
        savedInstanceState ?: supportFragmentManager.beginTransaction()
            .replace(R.id.container, CameraFragment(), "camera")
            .commitNowAllowingStateLoss()
    }
}
