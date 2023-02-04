package com.barrycallebaut.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.barrycallebaut.app.adapter.InspeksiAdapter
import com.barrycallebaut.app.adapter.PetaniAdapter
import com.barrycallebaut.app.databinding.FragmentInspeksiBinding
import com.barrycallebaut.app.databinding.FragmentSensusBinding
import com.barrycallebaut.app.ui.PetaniActivity
import com.barrycallebaut.app.ui.TambahInspeksiActivity

class InspeksiFragment : Fragment() {
    private var _binding: FragmentInspeksiBinding? = null
    private val binding get() = _binding!!

    private lateinit var inspeksiAdapter: InspeksiAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInspeksiBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.llTambah.setOnClickListener {
            val intent = Intent(context, TambahInspeksiActivity::class.java)
            startActivity(intent)
        }

//        val rv_inspeksi = binding.rvInspeksi
//        rv_inspeksi.layoutManager = LinearLayoutManager(activity)
//        inspeksiAdapter = InspeksiAdapter()
//        rv_inspeksi.adapter = inspeksiAdapter

        return view
    }
}