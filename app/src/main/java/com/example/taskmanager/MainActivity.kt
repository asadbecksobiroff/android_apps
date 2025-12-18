package com.example.taskmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var db: TaskDatabase
    private lateinit var adapter: TaskAdapter
    private var tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = TaskDatabase.getDatabase(this)

        val input = findViewById<EditText>(R.id.taskInput)
        val addBtn = findViewById<Button>(R.id.addTaskBtn)
        val recycler = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = TaskAdapter(tasks) { task ->
            deleteTask(task)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        loadTasks()

        addBtn.setOnClickListener {
            val text = input.text.toString()
            if (text.isNotEmpty()) {
                addTask(text)
                input.text.clear()
            }
        }
    }

    private fun loadTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            tasks.clear()
            tasks.addAll(db.taskDao().getAll())
            withContext(Dispatchers.Main) {
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun addTask(title: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db.taskDao().insert(Task(title = title))
            loadTasks()
        }
    }

    private fun deleteTask(task: Task) {
        CoroutineScope(Dispatchers.IO).launch {
            db.taskDao().delete(task)
            loadTasks()
        }
    }
}
