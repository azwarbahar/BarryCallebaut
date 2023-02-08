package com.barrycallebaut.app.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.barrycallebaut.app.R
import com.barrycallebaut.app.database.lokal.PreferencesHelper
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityAkunPetugasBinding
import com.barrycallebaut.app.models.Karyawan
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

class AkunPetugasActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityAkunPetugasBinding

    private lateinit var sharedPref: PreferencesHelper
    private var petugas_id: String = ""
    private var role: String = ""

    private lateinit var karyawan: Karyawan

    private lateinit var swipe_refresh: SwipeRefreshLayout

    private val PROFILE_IMAGE_REQ_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunPetugasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        petugas_id = sharedPref.getString(Constant.ID_USER).toString()
        role = sharedPref.getString(Constant.ROLE).toString()

        binding.imgBack.setOnClickListener {
            finish()
        }

        swipe_refresh = binding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_refresh.post(Runnable {
            loadData(petugas_id)
        })

        binding.rlLogout.setOnClickListener {

            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Keluar Akun")
                .setContentText("Yakin ingin keluar dari akun ini ?")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()

                    sharedPref.logout()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                .setCancelButton("Batal", SweetAlertDialog.OnSweetClickListener {
                    it.dismiss()
                })
                .show()
        }

        binding.cvPhoto.setOnClickListener {
            val pilihan = arrayOf(
                "Ganti Gambar",
                "Lihat Gambar"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Opsi")
            builder.setItems(pilihan) { dialog, which ->
                when (which) {
                    0 -> openImagePicker(PROFILE_IMAGE_REQ_CODE)
                    1 -> openPreviewImage()
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.llEditPassword.setOnClickListener {
            val intent = Intent(this, UbahPasswordActivity::class.java)
            startActivity(intent)
        }

        setupView()

    }

    private fun setupView() {

        binding.imgEditNama.setOnClickListener {
            var title = "Masukkan nama :"
            var type = "Text"
            var key = "nama"
            showDialogInput(title, type, key)
        }

        binding.imgEditJenisKelamin.setOnClickListener {
            var title = "Pilih jenis kelamin :"
            var key = "jenis_kelamin"
            showDialogRadio(title, key)
        }

        binding.imgEditTanggalLahir.setOnClickListener {
            showDatPickerDialig("tanggal_lahir")
        }

        binding.imgEditKontak.setOnClickListener {
            var title = "Masukkan Kontak :"
            var type = "Phone"
            var key = "kontak"
            showDialogInput(title, type, key)
        }

        binding.imgEditAlamat.setOnClickListener {
            var title = "Masukkan Alamat :"
            var type = "Text"
            var key = "alamat"
            showDialogInput(title, type, key)
        }

    }

    private fun showDatPickerDialig(key: String) {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = this?.let {
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
                    startUpdateData(petugas_id, key, tgl_send)
                    binding.tvTanggalLahir.setText(tgl_view)


                }, year, month, day
            )
        }
        dpd?.show()
    }

    private fun showDialogRadio(title: String, key: String) {
        val builder = android.app.AlertDialog.Builder(this)

        builder.setTitle(title)

        val view = layoutInflater.inflate(R.layout.dialog_jenis_kelamin, null)
        val genderGroup = view.findViewById<RadioGroup>(R.id.jenis_kelamin_group)

        builder.setView(view)

        builder.setPositiveButton("OK") { dialog, which ->
            // Action for "OK".
            val selectedGenderId = genderGroup.checkedRadioButtonId
            val selectedGender = if (selectedGenderId == R.id.rb_laki) "Laki-laki" else "Perempuan"
            setViewAfterSave(key, selectedGender)
            startUpdateData(petugas_id, key, selectedGender)

        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            // Action for "Cancel".
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun showDialogInput(title: String, type: String, key: String) {

        val builder = android.app.AlertDialog.Builder(this)

        builder.setTitle(title)

        val view = layoutInflater.inflate(R.layout.dialog_input_single, null)
        val input = view.findViewById<EditText>(R.id.et_value)

        if (type.equals("Text")) {
            input.inputType = InputType.TYPE_CLASS_TEXT
        } else if (type.equals("Number")) {
            input.inputType = InputType.TYPE_CLASS_NUMBER
        } else if (type.equals("Phone")) {
            input.inputType = InputType.TYPE_CLASS_PHONE
        } else {
            input.inputType = InputType.TYPE_CLASS_TEXT
        }

        builder.setView(view)

        builder.setPositiveButton("Simpan") { dialog, which ->
            // Action for "OK".
            val inputText = input.text.toString()
            setViewAfterSave(key, inputText)
            startUpdateData(petugas_id, key, inputText)
        }

        builder.setNegativeButton("Batal") { dialog, which ->
            // Action for "Cancel".
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun setViewAfterSave(key: String, inputText: String) {

        if (key.equals("nama")) {
            binding.tvNama.setText(inputText)
        } else if (key.equals("jenis_kelamin")) {
            binding.tvJekel.setText(inputText)
        } else if (key.equals("kontak")) {
            binding.tvKontak.setText(inputText)
        } else if (key.equals("alamat")) {
            binding.tvAlamat.setText(inputText)
        }

    }

    private fun startUpdateData(petani_id: String, key: String, value: String) {

        ApiClient.instances.updateKaryawanOneData(petani_id, key, value)
            ?.enqueue(object : Callback<Responses.ResponseKaryawan> {
                override fun onResponse(
                    call: Call<Responses.ResponseKaryawan>,
                    response: Response<Responses.ResponseKaryawan>
                ) {
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            Toast.makeText(
                                this@AkunPetugasActivity,
                                "Berhasil mengirim data!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {
//                            Toast.makeText(activity, "Berhasil mengirim data!", Toast.LENGTH_SHORT).show()
                        }

                    } else {

                    }

                }

                override fun onFailure(call: Call<Responses.ResponseKaryawan>, t: Throwable) {

                }

            })

    }


    private fun openPreviewImage() {

        var foto_intent = Constant.URL_PHOTO + karyawan.foto.toString()
        val intent = Intent(this, PreviewPhotoActivity::class.java)
        intent.putExtra("foto", foto_intent)
        startActivity(intent)

    }

    private fun openImagePicker(s: Int) {
        ImagePicker.with(this)
            .cropSquare()
            .compress(2048)
            .maxResultSize(1000, 1000)
            .start(PROFILE_IMAGE_REQ_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            if (requestCode == PROFILE_IMAGE_REQ_CODE) {
                binding.imgPhoto.setImageURI(null)
                binding.imgPhoto.setImageURI(uri)
                startUploadPhoto(uri)
            }
        }
    }

    private fun startUploadPhoto(uri: Uri) {

        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        val file = File(uri.path)
        val foto = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val foto_send = MultipartBody.Part.createFormData("foto", file.name, foto)
        val user_id = RequestBody.create("text/plain".toMediaTypeOrNull(), petugas_id)

        ApiClient.instances.updatePhotoKaryawan(user_id, foto_send)
            ?.enqueue(object : Callback<Responses.ResponseKaryawan> {
                override fun onResponse(
                    call: Call<Responses.ResponseKaryawan>,
                    response: Response<Responses.ResponseKaryawan>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            loadData(petugas_id)
                            Toast.makeText(
                                this@AkunPetugasActivity,
                                "Success upload photo!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            Toast.makeText(this@AkunPetugasActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@AkunPetugasActivity, pesanRespon, Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseKaryawan>, t: Throwable) {
                    Toast.makeText(this@AkunPetugasActivity, t.message, Toast.LENGTH_SHORT)
                        .show()

                    dialogProgress.dismiss()
                }

            })

    }


    private fun loadData(petugasId: String) {

        ApiClient.instances.getKaryawanId(petugasId)
            ?.enqueue(object : Callback<Responses.ResponseKaryawan> {
                override fun onResponse(
                    call: Call<Responses.ResponseKaryawan>,
                    response: Response<Responses.ResponseKaryawan>
                ) {
                    swipe_refresh.isRefreshing = false
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            karyawan = response.body()?.result_karyawan!!
                            initData(karyawan)
                        }
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseKaryawan>, t: Throwable) {

                    swipe_refresh.isRefreshing = false
                }

            })
    }

    private fun initData(karyawan: Karyawan) {

        binding.tvNama.setText(karyawan.nama.toString())
        binding.tvJekel.setText(karyawan.jenis_kelamin.toString())
        binding.tvPosisi.setText(karyawan.posisi.toString())
        binding.tvId.setText(karyawan.no_kepegawaian.toString())
        binding.tvTanggalLahir.setText(Constant.formatDate(karyawan.tanggal_lahir.toString()))
        binding.tvKontak.setText(karyawan.kontak.toString())
        binding.tvAlamat.setText(karyawan.alamat.toString())

        Glide.with(this)
            .load(Constant.URL_PHOTO + karyawan.foto.toString())
            .into(binding.imgPhoto)


    }


    override fun onRefresh() {
        loadData(petugas_id)
    }
}