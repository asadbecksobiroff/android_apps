package com.example.debughelper

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private val TAG = "DebugHelper"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnDebug).setOnClickListener {
            Log.d(TAG, "DEBUG: Tugma bosildi")
        }

        findViewById<Button>(R.id.btnInfo).setOnClickListener {
            Log.i(TAG, "INFO: Ma’lumot logi")
        }

        findViewById<Button>(R.id.btnError).setOnClickListener {
            Log.e(TAG, "ERROR: Xato logi")
        }

        findViewById<Button>(R.id.btnCrash).setOnClickListener {
            Log.e(TAG, "Ataylab crash qilinyapti")
            throw RuntimeException("Test crash")
        }
    }
}
