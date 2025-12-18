package com.example.twofragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        val tvResult = view.findViewById<TextView>(R.id.tvResult)
        val receivedData = arguments?.getString("data")

        tvResult.text = receivedData ?: "Ma’lumot kelmadi"

        return view
    }
}
