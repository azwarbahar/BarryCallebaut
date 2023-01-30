package com.barrycallebaut.app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.barrycallebaut.app.databinding.FragmentInspeksiBinding
import com.barrycallebaut.app.databinding.FragmentSensusBinding

class InspeksiFragment : Fragment() {
    private var _binding: FragmentInspeksiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInspeksiBinding.inflate(inflater, container, false)
        val view = binding.root


        return view
    }
}