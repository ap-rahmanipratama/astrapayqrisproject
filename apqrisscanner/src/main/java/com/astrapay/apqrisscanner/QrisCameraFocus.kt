package com.astrapay.apqrisscanner

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

internal class CameraFocus(context: Context, attributeSet: AttributeSet) :
    SurfaceView(context, attributeSet) {

    val focusRect: RectF = RectF()

    init {
        holder.setFormat(PixelFormat.TRANSPARENT)
        holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                //Do Nothing
            }

            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                val cameraHeight: Int = height
                val cameraWidth: Int = width

                val rect = Rect()
                rect.left = 0
                rect.top = cameraHeight
                rect.right = cameraWidth
                rect.bottom = 0
                val canvas = holder.lockCanvas()

                println(
                    "OVERLAY FOCUS, SCREEN: width:$width height:$height, FOCUS-> " +
                            "LEFt: ${focusRect.left} TOP:${focusRect.top} r:${focusRect.right} b:${focusRect.bottom}"
                )

                holder.unlockCanvasAndPost(canvas)
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
            }
        })
    }
}