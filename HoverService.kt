package com.example.tablethoverpointer

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.WindowManager

class HoverService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var pointerView: PointerView

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        pointerView = PointerView(this)

        // Android'in yeni sürümlerine uygun pencere tipi (Ekran üstü çizim için)
        val layoutFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            // Dokunmaları engellemesin (arkadaki uygulamalar çalışmaya devam etsin) ve odaklanmasın
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }

        // Görünümü ekrana ekle
        windowManager.addView(pointerView, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::pointerView.isInitialized) {
            windowManager.removeView(pointerView)
        }
    }
}

