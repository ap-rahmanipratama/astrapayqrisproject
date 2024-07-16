package com.astrapay.apqrisscanner

import android.content.Context
import androidx.annotation.Keep


@Keep
data class ApScannerSetup(
    val context: Context?,
    val listener: ApScannerListener?,
)