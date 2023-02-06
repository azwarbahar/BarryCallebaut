package com.barrycallebaut.app.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.barrycallebaut.app.R
import com.barrycallebaut.app.databinding.ActivityTambahInspeksiBinding
import com.bumptech.glide.Glide

class TambahInspeksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahInspeksiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener {
            finish()
        }

        binding.llContentPersonal.visibility = View.GONE
        binding.llContent2.visibility = View.GONE
        binding.llContent3.visibility = View.GONE

        binding.rlTopAspek1.setOnClickListener {
            showhideView(binding.llContentPersonal, binding.imgArrowPersonal)
        }

        binding.rlTopAspek2.setOnClickListener {
            showhideView(binding.llContent2, binding.imgArrow2)
        }

        binding.rlTopAspek3.setOnClickListener {
            showhideView(binding.llContent3, binding.imgArrow3)
        }
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