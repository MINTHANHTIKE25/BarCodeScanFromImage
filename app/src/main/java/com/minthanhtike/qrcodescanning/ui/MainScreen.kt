package com.minthanhtike.qrcodescanning.ui

import android.Manifest.permission.CAMERA
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.minthanhtike.qrcodescanning.ui.cameraFeature.imageCropper.ImageCropping
import com.minthanhtike.qrcodescanning.ui.cameraFeature.noPermission.NoPermission
import com.minthanhtike.qrcodescanning.ui.cameraFeature.photoCapture.CameraScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen() {
    val cameraPermissions : PermissionState= rememberPermissionState(permission = CAMERA)
    MainContent(
        hasPermission = cameraPermissions.status.isGranted,
        permissionRequest = { cameraPermissions.launchPermissionRequest() }
    )
}

@Composable
fun MainContent(
    hasPermission:Boolean,
    permissionRequest:()->Unit
) {
    if (hasPermission){
        ImageCropping()
    }else{
        NoPermission(permissionRequest,hasPermission)
    }
}