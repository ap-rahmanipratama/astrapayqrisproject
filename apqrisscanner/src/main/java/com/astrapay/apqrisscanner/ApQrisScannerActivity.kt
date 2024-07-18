package com.astrapay.apqrisscanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.astrapay.apqrisscanner.databinding.QrisScannerActivityBinding

class ApQrisScannerActivity : AppCompatActivity() {

    private lateinit var binding: QrisScannerActivityBinding

    private val intentRequestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                // granted, next process
                binding.cameraOverlay.startCamera(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = QrisScannerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkPermission(
                Manifest.permission.CAMERA,
                intentRequestPermission
            )
        ) {
            binding.cameraOverlay.startCamera(this)
        }

        binding.back.setOnClickListener {
            this.finish()
            ApQrisScanner.listener?.onComplete(type = EventType.Home)
        }

        binding.cameraOverlay.onGetQrResult {
            this.finish()
            ApQrisScanner.listener?.onQrisScanned(it)
        }
    }

    fun checkPermission(permission: String, launcher: ActivityResultLauncher<String>): Boolean {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            launcher.launch(permission)
            return false
        } else {
            return true
        }
    }
}

