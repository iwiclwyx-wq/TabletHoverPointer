package com.example.tablethoverpointer

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

class HoverService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var pointerView: PointerView

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        pointerView = PointerView(this)

        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        // DİKKAT: FLAG_NOT_TOUCHABLE kaldırıldı ki kalem/dokunma hareketlerini yakalayabilelim!
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

        // Kalem ekrana yaklaşırken veya hareket ederken tetiklenecek olay dinleyicisi
        pointerView.setOnGenericMotionListener { _, event ->
            if (event.isFromSource(android.view.InputDevice.SOURCE_MOUSE) ||
                event.isFromSource(android.view.InputDevice.SOURCE_STYLUS) ||
                event.isFromSource(android.view.InputDevice.SOURCE_TOUCHSCREEN)) {
                
                when (event.action) {
                    MotionEvent.ACTION_HOVER_ENTER,
                    MotionEvent.ACTION_HOVER_MOVE -> {
                        // Kalemin ekran üzerindeki X ve Y koordinatlarını al ve imleci güncelle
                        pointerView.updatePosition(event.rawX, event.rawY, true)
                        true
                    }
                    MotionEvent.ACTION_HOVER_EXIT -> {
                        // Kalem ekran alanından tamamen uzaklaştığında imleci gizle
                        pointerView.updatePosition(0f, 0f, false)
                        true
                    }
                    else -> false
                }
            } else {
                false
            }
        }

        windowManager.addView(pointerView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::pointerView.isInitialized) {
            windowManager.removeView(pointerView)
        }
    }
}
