package com.example.loginform

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private val TAG = "LoginForm"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etUsername = findViewById<TextInputEditText>(R.id.etUsername)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {

            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            Log.d(TAG, "Username: $username, Password: $password")

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Barcha maydonlarni to‘ldiring", Toast.LENGTH_SHORT).show()
            } else if (username == "admin" && password == "1234") {
                Toast.makeText(this, "Muvaffaqiyatli login", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Login yoki parol xato", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
