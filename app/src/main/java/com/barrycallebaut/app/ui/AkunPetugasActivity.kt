package com.barrycallebaut.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.barrycallebaut.app.databinding.ActivityAkunPetugasBinding

class AkunPetugasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAkunPetugasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunPetugasBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}