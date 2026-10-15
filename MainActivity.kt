package com.example.tablethoverpointer

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val OVERLAY_PERMISSION_REQ_CODE = 1234

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Basit bir arayüz oluşturalım (Buton ile izin isteyeceğiz)
        val button = Button(this).apply {
            text = "Ekran Üstü İzin Ver ve Başlat"
            setOnClickListener {
                checkOverlayPermission()
            }
        }
        setContentView(button)
    }

    private fun checkOverlayPermission() {
        // Android'in diğer uygulamalar üzerinde çizim yapma iznini kontrol et
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
        } else {
            startService()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(this)) {
                startService()
            } else {
                Toast.makeText(this, "İzin verilmedi!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startService() {
        val intent = Intent(this, HoverService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "Servis başlatıldı!", Toast.LENGTH_SHORT).show()
        finish() // Uygulamayı arka plana at
    }
}
