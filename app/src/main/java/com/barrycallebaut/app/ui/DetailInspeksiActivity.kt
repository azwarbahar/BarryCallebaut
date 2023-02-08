package com.barrycallebaut.app.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.barrycallebaut.app.R
import com.barrycallebaut.app.databinding.ActivityDetailInspeksiBinding
import com.barrycallebaut.app.models.Inspeksi
import com.barrycallebaut.app.utils.Constant
import com.bumptech.glide.Glide

class DetailInspeksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailInspeksiBinding

    private lateinit var inspeksi: Inspeksi

    private var posisi = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inspeksi = intent.getParcelableExtra("inspeksi")!!
        posisi = intent.getIntExtra("posisi", 0).toString()
        binding.tvToolbar.setText("Inspeksi " + posisi)

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

        initData(inspeksi)



        binding.imgDokumentasi.setOnClickListener {

            var foto_intent = Constant.URL_PHOTO + inspeksi.foto.toString()
            val intent = Intent(this, PreviewPhotoActivity::class.java)
            intent.putExtra("foto", foto_intent)
            startActivity(intent)

        }


    }

    private fun initData(inspeksi: Inspeksi) {
        setAspek1(inspeksi)

    }

    private fun setAspek1(inspeksi: Inspeksi) {

        var tanggal = inspeksi.tanggal.toString()
        binding.tvTanggal.setText(Constant.formatDate(tanggal))

        Glide.with(this)
            .load(Constant.URL_PHOTO + inspeksi.foto.toString())
            .into(binding.imgDokumentasi)

        var a1 = inspeksi.a1.toString()
        var a2 = inspeksi.a2.toString()
        var a3 = inspeksi.a3.toString()
        var a4 = inspeksi.a4.toString()
        var a5 = inspeksi.a5.toString()
        var a6 = inspeksi.a6.toString()
        var a7 = inspeksi.a7.toString()
        var a8 = inspeksi.a8.toString()
        var a9 = inspeksi.a9.toString()

        // A1
        if (a1.equals("1")) {
            binding.rbA11.isChecked = true
        } else if (a1.equals("2")) {
            binding.rbA12.isChecked = true
        } else if (a1.equals("3")) {
            binding.rbA13.isChecked = true
        } else if (a1.equals("4")) {
            binding.rbA14.isChecked = true
        } else if (a1.equals("5")) {
            binding.rbA15.isChecked = true
        }

        // A2
        if (a2.equals("1")) {
            binding.rbA21.isChecked = true
        } else if (a2.equals("2")) {
            binding.rbA22.isChecked = true
        } else if (a2.equals("3")) {
            binding.rbA23.isChecked = true
        } else if (a2.equals("4")) {
            binding.rbA24.isChecked = true
        } else if (a2.equals("5")) {
            binding.rbA25.isChecked = true
        }

        // A3
        if (a3.equals("1")) {
            binding.rbA31.isChecked = true
        } else if (a3.equals("2")) {
            binding.rbA32.isChecked = true
        } else if (a3.equals("3")) {
            binding.rbA33.isChecked = true
        } else if (a3.equals("4")) {
            binding.rbA34.isChecked = true
        } else if (a3.equals("5")) {
            binding.rbA35.isChecked = true
        }

        // A4
        if (a4.equals("1")) {
            binding.rbA41.isChecked = true
        } else if (a4.equals("2")) {
            binding.rbA42.isChecked = true
        } else if (a4.equals("3")) {
            binding.rbA43.isChecked = true
        } else if (a4.equals("4")) {
            binding.rbA44.isChecked = true
        } else if (a4.equals("5")) {
            binding.rbA45.isChecked = true
        }

        // A5
        if (a5.equals("1")) {
            binding.rbA51.isChecked = true
        } else if (a5.equals("2")) {
            binding.rbA52.isChecked = true
        } else if (a5.equals("3")) {
            binding.rbA53.isChecked = true
        } else if (a5.equals("4")) {
            binding.rbA54.isChecked = true
        } else if (a5.equals("5")) {
            binding.rbA55.isChecked = true
        }

        // A6
        if (a6.equals("1")) {
            binding.rbA61.isChecked = true
        } else if (a6.equals("2")) {
            binding.rbA62.isChecked = true
        } else if (a6.equals("3")) {
            binding.rbA63.isChecked = true
        } else if (a6.equals("4")) {
            binding.rbA64.isChecked = true
        } else if (a6.equals("5")) {
            binding.rbA65.isChecked = true
        }

        // A7
        if (a7.equals("1")) {
            binding.rbA71.isChecked = true
        } else if (a7.equals("2")) {
            binding.rbA72.isChecked = true
        } else if (a7.equals("3")) {
            binding.rbA73.isChecked = true
        } else if (a7.equals("4")) {
            binding.rbA74.isChecked = true
        } else if (a7.equals("5")) {
            binding.rbA75.isChecked = true
        }

        // A8
        if (a8.equals("1")) {
            binding.rbA81.isChecked = true
        } else if (a8.equals("2")) {
            binding.rbA82.isChecked = true
        } else if (a8.equals("3")) {
            binding.rbA83.isChecked = true
        } else if (a8.equals("4")) {
            binding.rbA84.isChecked = true
        } else if (a8.equals("5")) {
            binding.rbA85.isChecked = true
        }

        // A9
        if (a9.equals("1")) {
            binding.rbA91.isChecked = true
        } else if (a9.equals("2")) {
            binding.rbA92.isChecked = true
        } else if (a9.equals("3")) {
            binding.rbA93.isChecked = true
        } else if (a9.equals("4")) {
            binding.rbA94.isChecked = true
        } else if (a9.equals("5")) {
            binding.rbA95.isChecked = true
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