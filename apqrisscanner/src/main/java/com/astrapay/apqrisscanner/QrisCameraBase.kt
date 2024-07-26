package com.astrapay.apqrisscanner

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.Surface
import android.widget.FrameLayout
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.astrapay.apqrisscanner.databinding.QrisCameraBaseBinding
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class QrisCameraBase(mContext: Context, attributeSet: AttributeSet) :
    FrameLayout(mContext, attributeSet) {

    private var binding: QrisCameraBaseBinding

    private lateinit var imageProxy: ImageProxy
    private var cameraExecutor: ExecutorService
    private val optionScanner = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
        .build()
    private var callback: ((String) -> Unit)? = null
    private lateinit var camera: Camera
    private var isFlashEnabled = false

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = QrisCameraBaseBinding.inflate(inflater)
        binding.cameraOverlay.setZOrderMediaOverlay(true)
        cameraExecutor = Executors.newSingleThreadExecutor()
        addView(binding.root)
    }

    fun startCamera(lifecycleOwner: LifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val barcodeAnalyzer = ImageAnalysis.Builder()
                .setTargetRotation(Surface.ROTATION_90)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { it ->
                    it.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imgProxy ->
                        scanBarcode(imgProxy)
                    })
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    barcodeAnalyzer
                )

            } catch (exc: Exception) {

            }

        }, ContextCompat.getMainExecutor(context))
    }

    var status = false;

    @SuppressLint("UnsafeOptInUsageError")
    private fun scanBarcode(paramImageProxy: ImageProxy) {
        this.imageProxy = paramImageProxy
        val image = InputImage.fromMediaImage(
            imageProxy.image!!,
            imageProxy.imageInfo.rotationDegrees
        )

        val scanner = BarcodeScanning.getClient(optionScanner)
        scanner.process(image).addOnSuccessListener {
            status = it.size > 0
            it.forEach { barcode ->

                val scaleX = binding.cameraOverlay.width.toFloat() / image.height
                val scaleY = binding.cameraOverlay.height.toFloat() / image.width

                val br = barcode.boundingBox!!
                val left = br.left // * scaleX
                val top = br.top // * scaleY
                val isOnScan = left >= binding.cameraOverlay.focusRect.left
                        && left <= (binding.cameraOverlay.focusRect.right - binding.cameraOverlay.focusRect.width() / 2)
                        && top >= binding.cameraOverlay.focusRect.top
                        && top <= (binding.cameraOverlay.focusRect.bottom - binding.cameraOverlay.focusRect.height() / 2)

                //val msg = ("[$isOnScan] BoundingBox POS: (Left:${left}, Top:${top}), image: (${image.width}, ${image.height}) rotation:${imageProxy.imageInfo.rotationDegrees}")
                //if ( isOnScan ) {
                val msg =
                    "result---> ${barcode.valueType} -> ${barcode.rawValue} -> ${barcode.displayValue}"
                callback?.invoke(barcode.rawValue ?: "")
                //show(msg)
                //}
            }
        }.addOnFailureListener {
            it.printStackTrace()
            paramImageProxy.close()
        }.addOnCompleteListener {
            if (!status) {
                paramImageProxy.close()
            }
        }
    }

    fun getQRText(uri: Uri) {
        try {
            val image = InputImage.fromFilePath(context, uri)
            val scanner = BarcodeScanning.getClient(optionScanner)

            scanner.process(image)
                .addOnSuccessListener {
                    it.forEach { barcode ->
                        callback?.invoke(barcode.rawValue ?: "")
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    callback?.invoke("")
                }
        } catch (e: IOException) {
            e.printStackTrace()
            callback?.invoke("")
        }
    }

    fun onGetQrResult(callback: (String) -> Unit) {
        this.callback = callback
    }

    fun restartCameraScanner() {
        this.imageProxy.close()
    }

    fun switchCameraFlash() {
        if (camera.cameraInfo.hasFlashUnit()) {
            isFlashEnabled = !isFlashEnabled
            camera.cameraControl.enableTorch(isFlashEnabled)
        }
    }

    fun turnOffCameraFlash() {
        if (isFlashEnabled) isFlashEnabled = false
        camera.cameraControl.enableTorch(isFlashEnabled)
    }

}