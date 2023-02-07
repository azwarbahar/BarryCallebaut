package com.barrycallebaut.app.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.barrycallebaut.app.R
import com.barrycallebaut.app.database.lokal.PreferencesHelper
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityTambahInspeksiBinding
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.utils.Constant
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class TambahInspeksiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahInspeksiBinding


    private lateinit var sharedPref: PreferencesHelper
    private lateinit var petani: Petani
    private var petani_id: String = ""
    private var petugas_id: String = "7"

    private val IMAGE_REQ_CODE = 102

    var isDokumentationReady = false

    private lateinit var uri: Uri

    private var a1: String = ""
    private var a2: String = ""
    private var a3: String = ""
    private var a4: String = ""
    private var a5: String = ""
    private var a6: String = ""
    private var a7: String = ""
    private var a8: String = ""
    private var a9: String = ""
    private var tanggal_pilihan: String = ""
    private var tahun_pilihan: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahInspeksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        petani_id = sharedPref.getString(Constant.ID_PETANI_SELECTED).toString()

        initView()

    }

    private fun openImagePicker() {
        ImagePicker.with(this)
            .crop()
            .compress(2048)
            .maxResultSize(1000, 1000)
            .start(IMAGE_REQ_CODE)
    }

    private fun showDatPickerDialig() {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = this.let {
            DatePickerDialog(
                it,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    var dayfix = "" + dayOfMonth;
                    var monthfix = "" + monthOfYear;

                    if (monthOfYear < 10) {
                        monthfix = "0" + monthfix;
                    }
                    if (dayOfMonth < 10) {
                        dayfix = "0" + dayfix;
                    }
                    var tgl_send = "" + year + "-" + monthfix + "-" + dayfix
                    var tgl_view = "" + dayfix + "-" + monthfix + "-" + year
                    binding.tvTanggal.setText(tgl_view)
                    tanggal_pilihan = tgl_send
                    tahun_pilihan = "" + year

                }, year, month, day
            )
        }
        dpd?.show()
    }

    private fun initView() {

        binding.imgBack.setOnClickListener {
            onBackPressed()
//            finish()
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

        binding.cvTanggal.setOnClickListener {
            showDatPickerDialig()
        }


        binding.llAmbilGambar.setOnClickListener {
            openImagePicker()
        }

        binding.rgA1.setOnCheckedChangeListener { group, i ->
            val radioButton = findViewById<RadioButton>(i)
            val position = binding.rgA1.indexOfChild(radioButton) + 1
            a1 = position.toString()
        }

        binding.rgA2.setOnCheckedChangeListener { group, i ->
            val radioButton = findViewById<RadioButton>(i)
            val position = binding.rgA2.indexOfChild(radioButton) + 1
            a2 = position.toString()
        }

        binding.rgA3.setOnCheckedChangeListener { group, i ->
            val radioButton = findViewById<RadioButton>(i)
            val position = binding.rgA3.indexOfChild(radioButton) + 1
            a3 = position.toString()
        }

        binding.rgA4.setOnCheckedChangeListener { group, i ->
            val radioButton = findViewById<RadioButton>(i)
            val position = binding.rgA4.indexOfChild(radioButton) + 1
            a4 = position.toString()
        }

        binding.rgA5.setOnCheckedChangeListener { group, i ->
            val radioButton = findViewById<RadioButton>(i)
            val position = binding.rgA5.indexOfChild(radioButton) + 1
            a5 = position.toString()
        }

        binding.rgA6.setOnCheckedChangeListener { group, i ->
            val radioButton = findViewById<RadioButton>(i)
            val position = binding.rgA6.indexOfChild(radioButton) + 1
            a6 = position.toString()
        }

        binding.rgA7.setOnCheckedChangeListener { group, i ->
            val radioButton = findViewById<RadioButton>(i)
            val position = binding.rgA7.indexOfChild(radioButton) + 1
            a7 = position.toString()
        }

        binding.rgA8.setOnCheckedChangeListener { group, i ->
            val radioButton = findViewById<RadioButton>(i)
            val position = binding.rgA8.indexOfChild(radioButton) + 1
            a8 = position.toString()
        }

        binding.rgA9.setOnCheckedChangeListener { group, i ->
            val radioButton = findViewById<RadioButton>(i)
            val position = binding.rgA9.indexOfChild(radioButton) + 1
            a9 = position.toString()
        }

        binding.rlKirim.setOnClickListener {
            clickKirim()
        }

    }

    private fun clickKirim() {

        SweetAlertDialog(
            this,
            SweetAlertDialog.WARNING_TYPE
        )
            .setTitleText("Mengirim Data")
            .setContentText("Yakin ingin mengirim data inspeksi?")
            .setConfirmButton(
                "Ya"
            ) { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
                checkSend()
            }
            .setCancelButton("Batal") { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
            }
            .show()

    }

    private fun checkSend() {

        if (tanggal_pilihan.equals("") || tanggal_pilihan.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Lengkapi Tanggal!")
                .show()
        } else if (a1.equals("") || a1.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Keaktifan belum dilengkapi!")
                .show()
        } else if (a2.equals("") || a2.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Penanganan limbah pertanian belum dilengkapi!")
                .show()
        } else if (a3.equals("") || a3.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Tidak melakukan pemabatan berlebihan belum dilengkapi!")
                .show()
        } else if (a4.equals("") || a4.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Tidak menggunakan pestisida terlarang belum dilengkapi!")
                .show()
        } else if (a5.equals("") || a5.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Melakukan pemangkasan rutin belum dilengkapi!")
                .show()
        } else if (a6.equals("") || a6.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Penanganan dahan terserang penyakit belum dilengkapi!")
                .show()
        } else if (a7.equals("") || a7.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Penanganan buah terserang penyakit belum dilengkapi!")
                .show()
        } else if (a8.equals("") || a8.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Rata-rata jumlah buah kakao belum dilengkapi!")
                .show()
        } else if (a9.equals("") || a9.equals("null")) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Intensitas pemupukan (Satuan Kg) belum dilengkapi!")
                .show()
        } else if (!isDokumentationReady) {
            SweetAlertDialog(
                this,
                SweetAlertDialog.ERROR_TYPE
            )
                .setTitleText("Gagal")
                .setContentText("Dokumentasi belum dilengkapi!")
                .show()
        } else {
            startSend()
        }
    }

    private fun startSend() {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        val tanggal_send = RequestBody.create("text/plain".toMediaTypeOrNull(), tanggal_pilihan)
        val tahun_send = RequestBody.create("text/plain".toMediaTypeOrNull(), tahun_pilihan)
        val petani_id_send = RequestBody.create("text/plain".toMediaTypeOrNull(), petani_id)
        val petugas_id_send = RequestBody.create("text/plain".toMediaTypeOrNull(), petugas_id)
        val a1_send = RequestBody.create("text/plain".toMediaTypeOrNull(), a1)
        val a2_send = RequestBody.create("text/plain".toMediaTypeOrNull(), a2)
        val a3_send = RequestBody.create("text/plain".toMediaTypeOrNull(), a3)
        val a4_send = RequestBody.create("text/plain".toMediaTypeOrNull(), a4)
        val a5_send = RequestBody.create("text/plain".toMediaTypeOrNull(), a5)
        val a6_send = RequestBody.create("text/plain".toMediaTypeOrNull(), a6)
        val a7_send = RequestBody.create("text/plain".toMediaTypeOrNull(), a7)
        val a8_send = RequestBody.create("text/plain".toMediaTypeOrNull(), a8)
        val a9_send = RequestBody.create("text/plain".toMediaTypeOrNull(), a9)

        val file = uri.path?.let { File(it) }
        val foto = file?.let { RequestBody.create("image/*".toMediaTypeOrNull(), it) }
        val foto_send =
            foto?.let { MultipartBody.Part.createFormData("foto", file.name, it) }

        ApiClient.instances.addInspeksi(
            tanggal_send,
            tahun_send,
            petani_id_send,
            petugas_id_send,
            a1_send,
            a2_send,
            a3_send,
            a4_send,
            a5_send,
            a6_send,
            a7_send,
            a8_send,
            a9_send,
            foto_send
        )?.enqueue(object : Callback<Responses.ResponseInspeksi> {
            override fun onResponse(
                call: Call<Responses.ResponseInspeksi>,
                response: Response<Responses.ResponseInspeksi>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                if (response.isSuccessful) {
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (kode.equals("1")) {
                        SweetAlertDialog(
                            this@TambahInspeksiActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Inspeksi Berhasil terkirim..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@TambahInspeksiActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@TambahInspeksiActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(call: Call<Responses.ResponseInspeksi>, t: Throwable) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@TambahInspeksiActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            uri = data?.data!!
            if (requestCode == IMAGE_REQ_CODE) {
                isDokumentationReady = true
                binding.imgDokumentasi.setImageURI(null)
                binding.imgDokumentasi.setImageURI(uri)
            }
        }
    }

    override fun onBackPressed() {
        SweetAlertDialog(
            this,
            SweetAlertDialog.WARNING_TYPE
        )
            .setTitleText("Batalkan Inspeksi")
            .setContentText("Anda yakin ingin membatalkan inspeksi ?")
            .setConfirmButton(
                "Ya"
            ) { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
                super.onBackPressed()
            }
            .setCancelButton("Tutup") { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
            }
            .show()

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