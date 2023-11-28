package com.minthanhtike.qrcodescanning.ui.cameraFeature.imageCropper

import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarCodeScanner {
    var result = mutableListOf<String>()
    fun scanBarcodes(image: InputImage, callBack: (String) -> Unit) {

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_ALL_FORMATS
            )
            .build()

        val scanner = BarcodeScanning.getClient(options)

        scanner.process(image)
            .addOnSuccessListener { barcodesList ->
                processBarCode(barcodesList, scanner, callBack)
            }
            .addOnFailureListener {
                Log.e("SCANFAILURE", "FAILSCAN")
            }
    }

    private fun processBarCode(
        barcodeList: List<Barcode>, scanner: BarcodeScanner,
        callBack: (String) -> Unit
    ) {
        if (barcodeList.isNotEmpty()) {
            for (barcode in barcodeList) {
                if (barcode.rawValue?.isNotEmpty() == true) {
                    result.add(barcode.rawValue!!)
                    if (result.first().isNotEmpty()){
                        callBack(result.first())
                        result.clear()
                        scanner.close()
                        break
                    }
                }
            }
        }
    }
}