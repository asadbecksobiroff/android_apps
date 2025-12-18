package com.example.wifimonitor

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация Wi-Fi менеджера
        wifiManager = applicationContext
            .getSystemService(Context.WIFI_SERVICE) as WifiManager

        val tvInfo = findViewById<TextView>(R.id.tvInfo)
        val btnScan = findViewById<Button>(R.id.btnScan)

        // Обработка нажатия кнопки
        btnScan.setOnClickListener {
            scanWifi(tvInfo)
        }

        // Запрос разрешения на доступ к местоположению
        requestLocationPermission()
    }

    @SuppressLint("MissingPermission")
    private fun scanWifi(tv: TextView) {
        if (!wifiManager.isWifiEnabled) {
            tv.text = "Wi-Fi o'chirilgan"
            return
        }

        val results = wifiManager.scanResults
        val text = StringBuilder()

        for (network in results) {
            text.append(
                "SSID: ${network.SSID}\n" +
                        "Signal darajasi: ${network.level} dBm\n" +
                        "Xavfsilik turi: ${network.capabilities}\n\n"
            )
        }

        tv.text = text.toString()
    }

    private fun requestLocationPermission() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }
    }
}