package com.example.settingsapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean("dark_mode", false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES
            )
        } else {
            AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername: EditText = findViewById(R.id.etUsername)
        val switchDark: Switch = findViewById(R.id.switchDark)
        val btnSave: Button = findViewById(R.id.btnSave)

        etUsername.setText(sharedPref.getString("username", ""))
        switchDark.isChecked = isDarkMode

        btnSave.setOnClickListener {
            val editor = sharedPref.edit()
            editor.putString("username", etUsername.text.toString())
            editor.putBoolean("dark_mode", switchDark.isChecked)
            editor.apply()

            if (switchDark.isChecked) {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            } else {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }

            Toast.makeText(this, "Settings saved", Toast.LENGTH_SHORT).show()
        }
    }
}
