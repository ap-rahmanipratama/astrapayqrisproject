package com.astrapay.apqrisscanner

import android.content.Intent
import androidx.annotation.Keep

@Keep
object ApQrisScanner {
    private var isSetup = false
    var listener: ApScannerListener? = null

    @Throws(Exception::class)
    fun execute(apScannerSetup: ApScannerSetup) {
//        if (isSetup) {

            this.listener = apScannerSetup.listener
            val intent = Intent(apScannerSetup.context, ApQrisScannerActivity::class.java)
            apScannerSetup.context?.startActivity(intent)
//        } else {
//            throw Exception("AstraPay QRIS need to be setup")
//        }
    }
}