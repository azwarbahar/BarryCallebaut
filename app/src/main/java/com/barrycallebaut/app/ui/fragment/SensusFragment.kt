package com.barrycallebaut.app.ui.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.barrycallebaut.app.R
import com.barrycallebaut.app.databinding.FragmentSensusBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target


class SensusFragment : Fragment() {

    private var _binding: FragmentSensusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSensusBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.llContentPersonal.visibility = View.GONE
        binding.llContentAlamat.visibility = View.GONE

        binding.rlTopPersonal.setOnClickListener {
            showhideView(binding.llContentPersonal, binding.imgArrowPersonal)
        }

        binding.rlTopAlamat.setOnClickListener {
            showhideView(binding.llContentAlamat, binding.imgArrowAlamat)
        }


        return view
    }


    private fun showhideView(linearLayout: LinearLayout, imageView: ImageView) {
        if (linearLayout.visibility == View.VISIBLE) {
            Glide.with(this)
                .load(R.drawable.ic_baseline_keyboard_arrow_down_24)
                .into(imageView)
            linearLayout.animate()
                .translationY(0f)
                .alpha(0.0f)
                .setDuration(400)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        linearLayout.visibility = View.GONE
                    }
                })
        } else {
            Glide.with(this)
                .load(R.drawable.ic_baseline_keyboard_arrow_up_24)
                .into(imageView)
            linearLayout.visibility = View.VISIBLE
            linearLayout.animate()
                .translationY(0f)
                .alpha(1.0f)
                .setDuration(400)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                    }
                })
        }
    }


}