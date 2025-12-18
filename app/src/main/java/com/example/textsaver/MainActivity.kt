package com.example.textsaver

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.*

class MainActivity : AppCompatActivity() {

    private val fileName = "my_text.txt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etText: EditText = findViewById(R.id.etText)
        val btnSave: Button = findViewById(R.id.btnSave)
        val btnLoad: Button = findViewById(R.id.btnLoad)

        // Fayldan o'qish ilova ishga tushganda
        etText.setText(readFromFile())

        btnSave.setOnClickListener {
            val text = etText.text.toString()
            writeToFile(text)
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
        }

        btnLoad.setOnClickListener {
            etText.setText(readFromFile())
            Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writeToFile(data: String) {
        try {
            openFileOutput(fileName, MODE_PRIVATE).use {
                it.write(data.toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun readFromFile(): String {
        return try {
            openFileInput(fileName).bufferedReader().useLines { lines ->
                lines.joinToString("\n")
            }
        } catch (e: FileNotFoundException) {
            ""
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}
