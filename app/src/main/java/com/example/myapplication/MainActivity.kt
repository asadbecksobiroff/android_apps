package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.theme.MyApplicationTheme
import android.content.Context
import android.hardware.camera2.CameraManager
import android.widget.Button
import android.widget.EditText

import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String

    // Morse kodlari
    private val morseMap = mapOf(
        'A' to ".-",    'B' to "-...",  'C' to "-.-.",
        'D' to "-..",   'E' to ".",     'F' to "..-.",
        'G' to "--.",   'H' to "....",  'I' to "..",
        'J' to ".---",  'K' to "-.-",   'L' to ".-..",
        'M' to "--",    'N' to "-.",    'O' to "---",
        'P' to ".--.",  'Q' to "--.-",  'R' to ".-.",
        'S' to "...",   'T' to "-",     'U' to "..-",
        'V' to "...-",  'W' to ".--",   'X' to "-..-",
        'Y' to "-.--",  'Z' to "--..",

        '0' to "-----", '1' to ".----", '2' to "..---",
        '3' to "...--", '4' to "....-", '5' to ".....",
        '6' to "-....", '7' to "--...", '8' to "---..",
        '9' to "----."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]

        val txtInput = findViewById<EditText>(R.id.txtInput)
        val btnSend = findViewById<Button>(R.id.btnSend)

        btnSend.setOnClickListener {
            val text = txtInput.text.toString()
            if (text.isNotEmpty()) {
                sendMorse(text)
            }
        }
    }

    private fun sendMorse(text: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val upper = text.uppercase()

            for (char in upper) {
                val morse = morseMap[char]

                if (morse != null) {
                    for (symbol in morse) {
                        when (symbol) {
                            '.' -> emitFlash(200)
                            '-' -> emitFlash(600)
                        }
                        delay(200) // belgilar orasida
                    }
                    delay(600) // harflar orasida
                } else if (char == ' ') {
                    delay(800) // bo‘sh joy
                }
            }
        }
    }

    private suspend fun emitFlash(duration: Long) {
        cameraManager.setTorchMode(cameraId, true)
        delay(duration)
        cameraManager.setTorchMode(cameraId, false)
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}