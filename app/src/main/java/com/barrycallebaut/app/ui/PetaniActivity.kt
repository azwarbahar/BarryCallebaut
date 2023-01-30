package com.barrycallebaut.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.barrycallebaut.app.adapter.TabAdapter
import com.barrycallebaut.app.databinding.ActivityPetaniBinding
import com.google.android.material.tabs.TabLayoutMediator

class PetaniActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetaniBinding


    val tabArray = arrayOf(
        "Sensus",
        "Inspeksi"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetaniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupTabFragment()



    }

    private fun setupTabFragment() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val adapter = TabAdapter(supportFragmentManager, lifecycle, 2)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

    }

}