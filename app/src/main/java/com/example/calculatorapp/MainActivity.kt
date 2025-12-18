package com.example.calculatorapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Stack

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private var input = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvResult = findViewById(R.id.tvResult)

        // Id’lari shu formatda bo‘lishi kerak: btn0, btn1, ... btnPlus, btnMinus, btnMul, btnDiv, btnEqual, btnDot, btnClear
        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnPlus, R.id.btnMinus, R.id.btnMul, R.id.btnDiv, R.id.btnDot
        )

        // Raqamlar va operatorlar
        for (id in buttons) {
            findViewById<Button>(id).setOnClickListener {
                input += (it as Button).text
                tvResult.text = input
            }
        }

        // Clear
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            input = ""
            tvResult.text = "0"
        }

        // Equal
        findViewById<Button>(R.id.btnEqual).setOnClickListener {
            if (input.isNotEmpty()) {
                try {
                    val result = evaluateExpression(input)
                    tvResult.text = result.toString()
                    input = result.toString()
                } catch (e: Exception) {
                    tvResult.text = "Error"
                    input = ""
                }
            }
        }
    }

    // Oddiy arifmetik ifodani hisoblash (qo‘shimcha kutubxona ishlatmaydi)
    private fun evaluateExpression(expr: String): Double {
        val nums = Stack<Double>()
        val ops = Stack<Char>()
        var i = 0

        while (i < expr.length) {
            when (val c = expr[i]) {
                in '0'..'9', '.' -> {
                    var sb = ""
                    while (i < expr.length && (expr[i] in '0'..'9' || expr[i] == '.')) {
                        sb += expr[i]
                        i++
                    }
                    nums.push(sb.toDouble())
                    continue
                }
                '+', '-', '*', '/' -> {
                    while (ops.isNotEmpty() && precedence(ops.peek()) >= precedence(c)) {
                        val b = nums.pop()
                        val a = nums.pop()
                        nums.push(applyOp(ops.pop(), a, b))
                    }
                    ops.push(c)
                }
            }
            i++
        }

        while (ops.isNotEmpty()) {
            val b = nums.pop()
            val a = nums.pop()
            nums.push(applyOp(ops.pop(), a, b))
        }

        return if (nums.isNotEmpty()) nums.pop() else 0.0
    }

    private fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            else -> 0
        }
    }

    private fun applyOp(op: Char, a: Double, b: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> a / b
            else -> 0.0
        }
    }
}
