package com.minthanhtike.qrcodescanning.ui.cameraFeature.imageCropper

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.canhub.cropper.CropImage.CancelledResult.uriContent
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.minthanhtike.qrcodescanning.R
import kotlin.math.log

@Composable
fun ImageCropping() {
    val context = LocalContext.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var result by remember { mutableStateOf<String>("") }

    val imageCropLauncher = rememberLauncherForActivityResult(CropImageContract()) { cropResult ->
        if (cropResult.isSuccessful) {
            imageUri = cropResult.uriContent

        } else {
            val error = cropResult.error
        }
    }

    if (imageUri != null) {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = 100.dp)
    ) {
        Row {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap?.asImageBitmap()!!,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Blue)
                        .size(150.dp)
                        .border(
                            width = 1.dp,
                            color = Color.Blue,
                            shape = CircleShape
                        )
                )
                val inputImage = InputImage.fromBitmap(bitmap!!, 0)
                BarCodeScanner().scanBarcodes(inputImage) {
                    result = it
                }
                if (result.isNotEmpty()){
                    Text(text = result)
                }
                Log.d("MINTHANHTIKCROP", "ImageCropping: $result")
            } else {
                Image(
                    painter = painterResource(id = R.drawable.baseline_person_24),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.Blue)
                        .size(150.dp)
                )
            }

            Image(
                painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                contentDescription = null,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .size(50.dp)
                    .padding(10.dp)
                    .clickable {
                        val cropOption =
                            CropImageContractOptions(uriContent, CropImageOptions())
                        imageCropLauncher.launch(cropOption)
                    }
            )
//            }
        }

    }

}

