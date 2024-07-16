package com.astrapay.apqrisscanner

import androidx.annotation.Keep

@Keep
interface ApScannerListener {
    fun onComplete(type: EventType)
    fun onQrisScanned(data: String)
}

enum class EventType{
    @Keep Home,
    @Keep Detail
}