package com.example.profilepass

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvProfile = findViewById<TextView>(R.id.tvProfile)

        // Intent orqali ma'lumotlarni olish
        val username = intent.getStringExtra("USERNAME")
        val email = intent.getStringExtra("EMAIL")

        tvProfile.text = "Username: $username\nEmail: $email"
    }
}
