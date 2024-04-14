package com.example.windealer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.windealer.databinding.EmiBinding
import com.example.windealer.databinding.NoemiBinding

class EmiFragment():Fragment(R.layout.emi) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = EmiBinding.inflate(inflater,container,false)
        return binding.root
    }
}