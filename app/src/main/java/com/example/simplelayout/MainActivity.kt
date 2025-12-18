package com.example.simplelayout

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var btnSubmit: Button
    private lateinit var tvResult: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etName = findViewById(R.id.etName)
        btnSubmit = findViewById(R.id.btnSubmit)
        tvResult = findViewById(R.id.tvResult)

        btnSubmit.setOnClickListener {
            val name = etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(this, "Iltimos, ismingizni kiriting", Toast.LENGTH_SHORT).show()
            } else {
                tvResult.text = "Salom, $name!"
            }
        }
    }
}
