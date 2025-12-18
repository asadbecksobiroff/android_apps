package com.example.contactslist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val contacts = listOf(
            Contact("Ali Valiyev", "+998 90 123 45 67"),
            Contact("Vali Aliyev", "+998 91 765 43 21"),
            Contact("Hasan Husanov", "+998 93 111 22 33"),
            Contact("Olim Karimov", "+998 94 999 88 77")
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ContactAdapter(contacts)
    }
}
