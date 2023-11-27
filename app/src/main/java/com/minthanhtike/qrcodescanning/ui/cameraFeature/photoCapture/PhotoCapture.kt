package com.minthanhtike.qrcodescanning.ui.cameraFeature.photoCapture

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.minthanhtike.qrcodescanning.coreUtils.ExtendedFunc.rotateBitmap
import java.util.concurrent.Executor

@Composable
fun CameraScreen(
    cameraVm : CameraViewModel = viewModel()
) {
    val cameraState by cameraVm.state.collectAsState()
    CameraContent(
        onPhotoCapture = {bitmap -> cameraVm.storePhotoInGallery(bitmap) },
        lastPhotoCapture = cameraState.capturedImage
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraContent(
    modifier: Modifier = Modifier,
    onPhotoCapture: (Bitmap) -> Unit,
    lastPhotoCapture: Bitmap? = null
) {
    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val cameraController = remember { LifecycleCameraController(context) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    capturePhoto(context, cameraController, onPhotoCapture)
                })
            {
                Icon(imageVector = Icons.Default.Camera, contentDescription = "take")
                Text(text = "Take Photo")
            }
        }
    ) { paddingValues ->
        Box(modifier = modifier.fillMaxSize()) {
            AndroidView(
                factory = { context ->
                    PreviewView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                        setBackgroundColor(android.graphics.Color.BLACK)
                        scaleType = PreviewView.ScaleType.FILL_START
                    }.also { previewView ->
                        previewView.controller = cameraController
                        cameraController.bindToLifecycle(lifeCycleOwner)
                    }
                },
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
            if (lastPhotoCapture != null) {
                LastPhotoPreview(
                    lastCapturedPhoto = lastPhotoCapture,
                    modifier = modifier.align(Alignment.BottomStart)
                )
            }
        }

    }
}

private fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val correctedBitmap: Bitmap = image
                .toBitmap()
                .rotateBitmap(image.imageInfo.rotationDegrees)

            onPhotoCaptured(correctedBitmap)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}

@Composable
private fun LastPhotoPreview(
    modifier: Modifier = Modifier,
    lastCapturedPhoto: Bitmap
) {
    var toPreviewClick by remember{ mutableStateOf(false) }

    val capturedPhoto: ImageBitmap = remember(lastCapturedPhoto.hashCode()) {
        lastCapturedPhoto.asImageBitmap()
    }
    Card(
        modifier = modifier
            .size(128.dp)
            .padding(16.dp)
            .clickable { toPreviewClick = true },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Image(
            bitmap = capturedPhoto,
            contentDescription = "Last captured photo",
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun FullPhotoScreen(modifier: Modifier=Modifier) {
    Surface(modifier = modifier.fillMaxSize(1f)) {

    }
}