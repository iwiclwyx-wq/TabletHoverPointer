package com.example.tablethoverpointer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class PointerView(context: Context) : View(context) {

    private val paint = Paint().apply {
        color = Color.RED // İmlecin rengi (kırmızı nokta)
        isAntiAlias = true
        style = Paint.Style.FILL
    }

    private var pointerX: Float = -100f
    private var pointerY: Float = -100f
    private var isVisible: Boolean = false

    // Kalemin konumunu güncelleyip ekranı yeniden çizen fonksiyon
    fun updatePosition(x: Float, y: Float, visible: Boolean) {
        pointerX = x
        pointerY = y
        isVisible = visible
        invalidate() // Ekranın yeniden çizilmesini tetikler
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isVisible) {
            // Belirtilen koordinatlara 15 piksel yarıçapında bir daire (imleç) çizer
            canvas.drawCircle(pointerX, pointerY, 15f, paint)
        }
    }
}

