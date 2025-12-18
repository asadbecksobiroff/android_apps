package com.example.profilepass

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val username = etUsername.text.toString()
            val email = etEmail.text.toString()

            if (username.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Iltimos, barcha maydonlarni to‘ldiring", Toast.LENGTH_SHORT).show()
            } else {
                // Intent orqali ma'lumot yuborish
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra("USERNAME", username)
                intent.putExtra("EMAIL", email)
                startActivity(intent)
            }
        }
    }
}
