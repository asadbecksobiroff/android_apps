package com.example.collectionsviewer

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private val items = listOf(
        "Olma", "Banan", "Anor", "Shaftoli", "O'rik",
        "Gilos", "Mandarin", "Nok", "Uzum", "Qulupnay"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.lvCollections)

        // Adapter yaratish
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            items
        )
        listView.adapter = adapter

        // Element bosilganda toast
        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            Toast.makeText(this, "Siz tanladingiz: $selectedItem", Toast.LENGTH_SHORT).show()
        }
    }
}
