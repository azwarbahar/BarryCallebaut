package com.barrycallebaut.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.barrycallebaut.app.databinding.ActivityPreviewPhotoBinding
import com.bumptech.glide.Glide

class PreviewPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewPhotoBinding

    private var foto: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        foto = intent.getStringExtra("foto")!!

        Glide.with(this)
            .load(foto)
            .into(binding.imgZoom)

        binding.imgClose.setOnClickListener { finish() }
    }
}