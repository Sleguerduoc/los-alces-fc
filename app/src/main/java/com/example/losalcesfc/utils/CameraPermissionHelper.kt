package com.example.losalcesfc.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object CameraPermissionHelper {
    fun ensureCameraPermission(
        context: Context,
        requestPermissionLauncher: ActivityResultLauncher<String>
    ): Boolean {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (!isGranted) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            return false
        }

        return true
    }
}
