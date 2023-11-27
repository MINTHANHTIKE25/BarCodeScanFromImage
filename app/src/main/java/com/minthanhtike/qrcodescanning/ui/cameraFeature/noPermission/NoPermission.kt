package com.minthanhtike.qrcodescanning.ui.cameraFeature.noPermission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat

@Composable
fun NoPermission(
    requestPermission: () -> Unit,
    hasPermission: Boolean
) {
    NoPermissionContent(hasPermission) {
        requestPermission()
    }
}

@Composable
fun NoPermissionContent(
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    val permissionState = remember { mutableStateListOf<Boolean>() }
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Please grant the permission to use the camera to use the core functionality of this app.")
        if (permissionState.size >= 1) {
            Button(onClick = {
                ContextCompat.startActivity(context, createSettingsIntent(context), null)
            }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                Text(text = "Go To Settings")
            }
        } else {
            Button(onClick = {
                onRequestPermission()
                permissionState.add(hasPermission)
                Log.d("PERMITSIZE", "NoPermissionContent: ${permissionState.size}")
            }) {
                Icon(imageVector = Icons.Default.Camera, contentDescription = "Camera")
                Text(text = "Grant permission")
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun NoPermissionPreview() {
//    NoPermissionContent() {
//
//    }
}

fun createSettingsIntent(context: Context): Intent {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        data = Uri.fromParts("package", context.packageName, null)
    }

    return intent
}