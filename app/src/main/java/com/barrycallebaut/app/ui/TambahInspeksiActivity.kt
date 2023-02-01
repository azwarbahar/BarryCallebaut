package com.barrycallebaut.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.barrycallebaut.app.databinding.ActivityTambahInspeksiBinding

class TambahInspeksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahInspeksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}