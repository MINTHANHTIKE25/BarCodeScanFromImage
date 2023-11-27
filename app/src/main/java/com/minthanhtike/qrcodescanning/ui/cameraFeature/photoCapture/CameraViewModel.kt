package com.minthanhtike.qrcodescanning.ui.cameraFeature.photoCapture

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel() : ViewModel() {
    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun storePhotoInGallery(bitmap: Bitmap){
        viewModelScope.launch {
            updateCapturedPhotoState(bitmap)
        }
    }

    private fun updateCapturedPhotoState(updatePhoto:Bitmap?){
        _state.value.capturedImage?.recycle()
        _state.value = _state.value.copy(capturedImage = updatePhoto)
    }

    override fun onCleared() {
        _state.value.capturedImage?.recycle()
        super.onCleared()
    }
}

data class CameraState(
    val capturedImage: Bitmap? = null,
)