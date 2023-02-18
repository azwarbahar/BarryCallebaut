package com.barrycallebaut.app.ui.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog
import com.barrycallebaut.app.R
import com.barrycallebaut.app.database.lokal.PreferencesHelper
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.FragmentSensusBinding
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.models.Sensus
import com.barrycallebaut.app.ui.PreviewPhotoActivity
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


class SensusFragment : Fragment() {

    private var _binding: FragmentSensusBinding? = null
    private val binding get() = _binding!!

    private lateinit var petani: Petani
    private lateinit var sensus: Sensus

    private lateinit var sharedPref: PreferencesHelper
    private var petani_id: String = ""

    private val IMAGE_REQ_CODE = 102

    var isDokumentationReady = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSensusBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPref = PreferencesHelper(context)
        petani_id = sharedPref.getString(Constant.ID_PETANI_SELECTED).toString()
        setupView()
        laodData(petani_id)
        return view
    }

    private fun openImagePicker() {
        ImagePicker.with(activity!!)
            .crop()
            .compress(2048)
            .maxResultSize(1000, 1000)
            .start(IMAGE_REQ_CODE)
    }

    private fun showDialogPendidikan(title: String, key: String) {
        val items = arrayOf(
            "Tidak Sekolah",
            "Tamat SD",
            "Tamat SMP/Sederajat",
            "Tamat SMA/Sederajat",
            "Sarjana Diploma",
            "S1",
            "S2",
            "S3"
        )
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setItems(items) { dialog, which ->
            var pilihan = items[which]
            setViewAfterSave(key, pilihan)
            startUpdateData(petani_id, key, pilihan)
        }
        val dialog = builder.create()
        dialog.show()

    }

    private fun showDialogRadio(title: String, key: String) {
        val builder = AlertDialog.Builder(activity)

        builder.setTitle(title)

        val view = layoutInflater.inflate(R.layout.dialog_jenis_kelamin, null)
        val genderGroup = view.findViewById<RadioGroup>(R.id.jenis_kelamin_group)

        builder.setView(view)

        builder.setPositiveButton("OK") { dialog, which ->
            // Action for "OK".
            val selectedGenderId = genderGroup.checkedRadioButtonId
            val selectedGender = if (selectedGenderId == R.id.rb_laki) "Laki-laki" else "Perempuan"
            setViewAfterSave(key, selectedGender)
            startUpdateData(petani_id, key, selectedGender)

        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            // Action for "Cancel".
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()

    }

    private fun showDialogInput(title: String, type: String, key: String) {

        val builder = AlertDialog.Builder(activity)

        builder.setTitle(title)

        val view = layoutInflater.inflate(R.layout.dialog_input_single, null)
        val input = view.findViewById<EditText>(R.id.et_value)

        if (type.equals("Text")) {
            input.inputType = InputType.TYPE_CLASS_TEXT
        } else if (type.equals("Number")) {
            input.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL
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
            startUpdateData(petani_id, key, inputText)
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
        } else if (key.equals("id_petani")) {
            binding.tvIdPetani.setText(inputText)
        } else if (key.equals("kontak")) {
            binding.tvKontak.setText(inputText)
        } else if (key.equals("jenis_kelamin")) {
            binding.tvJenisKelamin.setText(inputText)
        } else if (key.equals("pendidikan")) {
            binding.tvPendidikan.setText(inputText)
        } else if (key.equals("kelompok")) {
            binding.tvKelompok.setText(inputText)
        } else if (key.equals("alamat")) {
            binding.tvAlamat.setText(inputText)
        } else if (key.equals("kelurahan")) {
            binding.tvKelurahan.setText(inputText)
        } else if (key.equals("kecamatan")) {
            binding.tvKecamatan.setText(inputText)
        } else if (key.equals("jumlah_lahan")) {
            binding.tvJumlahLahan.setText(inputText)
        } else if (key.equals("luas_lahan")) {
            binding.tvLuasLahan.setText(inputText)
        } else if (key.equals("kakau_lokal")) {
            binding.tvKakaoLokal.setText(inputText)
        } else if (key.equals("kakao_s1")) {
            binding.tvKakaoS1.setText(inputText)
        } else if (key.equals("kakao_s2")) {
            binding.tvKakaoS2.setText(inputText)
        } else if (key.equals("jarak_tanah")) {
            binding.tvJarakTanam.setText(inputText)
        } else if (key.equals("hasil_panen")) {
            binding.tvHasilPanen.setText(inputText)
        }

    }

    private fun showDatPickerDialig(key: String) {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = activity?.let {
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
                    startUpdateData(petani_id, key, tgl_send)
                    if (key.equals("tanggal_sensus")) {
                        binding.tvTanggalSensus.setText(tgl_view)
                    } else if (key.equals("tanggal_lahir")) {
                        binding.tvTanggalLahir.setText(tgl_view)
                    }

                }, year, month, day
            )
        }
        dpd?.show()
    }

    private fun startUpdateData(petani_id: String, key: String, value: String) {

        ApiClient.instances.updatePetaniOneData(petani_id, key, value)
            ?.enqueue(object : Callback<Responses.ResponsePetani> {
                override fun onResponse(
                    call: Call<Responses.ResponsePetani>,
                    response: Response<Responses.ResponsePetani>
                ) {
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            Toast.makeText(activity, "Berhasil mengirim data!", Toast.LENGTH_SHORT)
                                .show()
                        } else {
//                            Toast.makeText(activity, "Berhasil mengirim data!", Toast.LENGTH_SHORT).show()
                        }

                    } else {

                    }

                }

                override fun onFailure(call: Call<Responses.ResponsePetani>, t: Throwable) {

                }

            })

    }

    private fun laodData(petaniId: String) {

        ApiClient.instances.getPetaniId(petaniId)
            ?.enqueue(object : Callback<Responses.ResponsePetani> {
                override fun onResponse(
                    call: Call<Responses.ResponsePetani>,
                    response: Response<Responses.ResponsePetani>
                ) {
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            petani = response.body()?.result_petani!!
                            initData(petani)
                        }
                    }
                }

                override fun onFailure(call: Call<Responses.ResponsePetani>, t: Throwable) {

                }

            })
    }

    private fun initData(petani: Petani) {
        setUpDataPersonal(petani)
        setUpDataAlamat(petani)
        setUpDataKebun(petani)

        // DOKUMENTASI
        var dokumentasi_sensus = petani.dokumentasi_sensus.toString()
        if (dokumentasi_sensus.equals("") || dokumentasi_sensus.equals("null")) {
            isDokumentationReady = false
            Glide.with(this)
                .load(R.drawable.img_empty)
                .into(binding.imgDokumentasi)
        } else {
            isDokumentationReady = true
            Glide.with(this)
                .load(Constant.URL_PHOTO + dokumentasi_sensus)
                .into(binding.imgDokumentasi)
        }

    }

    private fun setUpDataPersonal(petani: Petani) {

        var nama = petani.nama.toString()
        var jenis_kelamin = petani.jenis_kelamin.toString()
        var kode_petani = petani.id_petani.toString()
        var kontak = petani.kontak.toString()
        var tgl_lahir = petani.tanggal_lahir.toString()
        var pendidikan = petani.pendidikan.toString()
        var kelompok = petani.kelompok.toString()

        var tanggal = petani.tanggal_sensus.toString()
        if (tanggal.equals("") || tanggal.equals("null")) {
            binding.tvTanggalSensus.setText("----/--/--")
        } else {
            binding.tvTanggalSensus.setText(Constant.formatDate(tanggal))
        }


        if (nama.equals("") || nama.equals("null")) {
            binding.tvNama.setText("-")
        } else {
            binding.tvNama.setText(nama)
        }

        if (jenis_kelamin.equals("") || jenis_kelamin.equals("null")) {
            binding.tvJenisKelamin.setText("-")
        } else {
            binding.tvJenisKelamin.setText(jenis_kelamin)
        }

        if (kode_petani.equals("") || kode_petani.equals("null")) {
            binding.tvIdPetani.setText("-")
        } else {
            binding.tvIdPetani.setText(kode_petani)
        }

        if (kontak.equals("") || kontak.equals("null")) {
            binding.tvKontak.setText("-")
        } else {
            binding.tvKontak.setText(kontak)
        }

        if (tgl_lahir.equals("") || tgl_lahir.equals("null")) {
            binding.tvTanggalLahir.setText("-")
        } else {
            binding.tvTanggalLahir.setText(Constant.formatDate(tgl_lahir))
        }

        if (pendidikan.equals("") || pendidikan.equals("null")) {
            binding.tvPendidikan.setText("-")
        } else {
            binding.tvPendidikan.setText(pendidikan)
        }

        if (kelompok.equals("") || kelompok.equals("null")) {
            binding.tvKelompok.setText("-")
        } else {
            binding.tvKelompok.setText(kelompok)
        }
    }

    private fun setUpDataAlamat(petani: Petani) {

        var alamat = petani.alamat.toString()
        var kelurahan = petani.kelurahan.toString()
        var kecamatan = petani.kecamatan.toString()

        if (alamat.equals("") || alamat.equals("null")) {
            binding.tvAlamat.setText("-")
        } else {
            binding.tvAlamat.setText(alamat)
        }

        if (kelurahan.equals("") || kelurahan.equals("null")) {
            binding.tvKelurahan.setText("-")
        } else {
            binding.tvKelurahan.setText(kelurahan)
        }

        if (kecamatan.equals("") || kecamatan.equals("null")) {
            binding.tvKecamatan.setText("-")
        } else {
            binding.tvKecamatan.setText(kecamatan)
        }

    }

    private fun setUpDataKebun(petani: Petani) {

        var jumlah_lahan = petani.jumlah_lahan.toString()
        var luas_lahan = petani.luas_lahan.toString()
        var kakao_lokal = petani.kakau_lokal.toString()
        var kakao_s1 = petani.kakao_s1.toString()
        var kakao_s2 = petani.kakao_s2.toString()
        var jarak_tanam = petani.jarak_tanah.toString()
        var hasil_panen = petani.hasil_panen.toString()

        if (jumlah_lahan.equals("") || jumlah_lahan.equals("null")) {
            binding.tvJumlahLahan.setText("-")
        } else {
            binding.tvJumlahLahan.setText(jumlah_lahan)
        }

        if (luas_lahan.equals("") || luas_lahan.equals("null")) {
            binding.tvLuasLahan.setText("-")
        } else {
            binding.tvLuasLahan.setText(luas_lahan)
        }

        if (kakao_lokal.equals("") || kakao_lokal.equals("null")) {
            binding.tvKakaoLokal.setText("-")
        } else {
            binding.tvKakaoLokal.setText(kakao_lokal)
        }

        if (kakao_s1.equals("") || kakao_s1.equals("null")) {
            binding.tvKakaoS1.setText("-")
        } else {
            binding.tvKakaoS1.setText(kakao_s1)
        }

        if (kakao_s2.equals("") || kakao_s2.equals("null")) {
            binding.tvKakaoS2.setText("-")
        } else {
            binding.tvKakaoS2.setText(kakao_s2)
        }

        if (jarak_tanam.equals("") || jarak_tanam.equals("null")) {
            binding.tvJarakTanam.setText("-")
        } else {
            binding.tvJarakTanam.setText(jarak_tanam)
        }

        if (hasil_panen.equals("") || hasil_panen.equals("null")) {
            binding.tvHasilPanen.setText("-")
        } else {
            binding.tvHasilPanen.setText(hasil_panen)
        }

    }

    private fun setupView() {

        binding.llContentPersonal.visibility = View.GONE
        binding.llContentAlamat.visibility = View.GONE
        binding.llContentKebun.visibility = View.GONE

        binding.rlTopPersonal.setOnClickListener {
            showhideView(binding.llContentPersonal, binding.imgArrowPersonal)
        }

        binding.rlTopAlamat.setOnClickListener {
            showhideView(binding.llContentAlamat, binding.imgArrowAlamat)
        }

        binding.rlTopKebun.setOnClickListener {
            showhideView(binding.llContentKebun, binding.imgArrowKebun)
        }

        binding.imgEditTanggalSensus.setOnClickListener {
            showDatPickerDialig("tanggal_sensus")
        }

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

        binding.imgEditKontak.setOnClickListener {
            var title = "Masukkan Kontak :"
            var type = "Phone"
            var key = "kontak"
            showDialogInput(title, type, key)
        }

        binding.imgTanggalLahir.setOnClickListener {
            showDatPickerDialig("tanggal_lahir")
        }

        binding.imgPendidikan.setOnClickListener {
            var title = "Pilih Status Pendidikan :"
            var key = "pendidikan"
            showDialogPendidikan(title, key)
        }

        binding.imgKelompok.setOnClickListener {
            var title = "Masukkan Nama Kelompok :"
            var type = "Text"
            var key = "kelompok"
            showDialogInput(title, type, key)
        }

        binding.imgEditAlamat.setOnClickListener {
            var title = "Masukkan Alamat :"
            var type = "Text"
            var key = "alamat"
            showDialogInput(title, type, key)
        }

        binding.imgEditKelurahan.setOnClickListener {
            var title = "Masukkan Kelurahan :"
            var type = "Text"
            var key = "kelurahan"
            showDialogInput(title, type, key)
        }

        binding.imgEditKecamatan.setOnClickListener {
            var title = "Masukkan Kecamatan :"
            var type = "Text"
            var key = "kecamatan"
            showDialogInput(title, type, key)
        }

        binding.imgEditJumlahLahan.setOnClickListener {
            var title = "Jumlah Lahan (Lokasi) : "
            var type = "Number"
            var key = "jumlah_lahan"
            showDialogInput(title, type, key)
        }

        binding.imgEditLuasLahan.setOnClickListener {
            var title = "Luas Lahan (Hektar) : "
            var type = "Number"
            var key = "luas_lahan"
            showDialogInput(title, type, key)
        }

        binding.imgEditKakaoLokal.setOnClickListener {
            var title = "Kakao Lokal (Pohon) : "
            var type = "Number"
            var key = "kakau_lokal"
            showDialogInput(title, type, key)
        }

        binding.imgEditKakaoS1.setOnClickListener {
            var title = "Kakao S1 (Pohon) : "
            var type = "Number"
            var key = "kakao_s1"
            showDialogInput(title, type, key)
        }

        binding.imgEditKakaoS2.setOnClickListener {
            var title = "Kakao S2 (Pohon) : "
            var type = "Number"
            var key = "kakao_s2"
            showDialogInput(title, type, key)
        }

        binding.imgEditJarakTanam.setOnClickListener {
            var title = "Jarak Tanah (Meter) : "
            var type = "Number"
            var key = "jarak_tanah"
            showDialogInput(title, type, key)
        }

        binding.imgEditHasilPanen.setOnClickListener {
            var title = "Hasil Panen (Kg) : "
            var type = "Number"
            var key = "hasil_panen"
            showDialogInput(title, type, key)
        }

        binding.llAmbilGambar.setOnClickListener {
            if (!isDokumentationReady) {
                openImagePicker()
            }
        }

        binding.cvOption.setOnClickListener {
            if (isDokumentationReady) {
                val pilihan = arrayOf(
                    "Ganti",
                    "Hapus"
                )
                val builder: androidx.appcompat.app.AlertDialog.Builder =
                    androidx.appcompat.app.AlertDialog.Builder(activity!!)
                builder.setTitle("Opsi")
                builder.setItems(pilihan) { dialog, which ->
                    when (which) {
                        0 -> openImagePicker()
                        1 -> deleteDoctSensus()
                    }
                }
                val dialog = builder.create()
                dialog.show()
            }
        }

        binding.imgDokumentasi.setOnClickListener {

            // DOKUMENTASI
            var dokumentasi_sensus = petani.dokumentasi_sensus.toString()
            if (dokumentasi_sensus.equals("") || dokumentasi_sensus.equals("null")) {

            } else {
                var foto_intent = Constant.URL_PHOTO + dokumentasi_sensus
                val intent = Intent(activity, PreviewPhotoActivity::class.java)
                intent.putExtra("foto", foto_intent)
                startActivity(intent)
            }
        }

    }

    private fun deleteDoctSensus() {
        ApiClient.instances.deleteDoctSensus(petani_id)
            ?.enqueue(object : Callback<Responses.ResponsePetani> {
                override fun onResponse(
                    call: Call<Responses.ResponsePetani>,
                    response: Response<Responses.ResponsePetani>
                ) {
                    if (response.isSuccessful) {
                        var kode = response.body()?.kode
                        if (kode.equals("1")) {
                            isDokumentationReady = false
                            Glide.with(activity!!)
                                .load(R.drawable.img_empty)
                                .into(binding.imgDokumentasi)
                            Toast.makeText(
                                activity,
                                "Berhasil menghapus dokumentasi",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(activity, "Gagal menghapus!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "Gagal, Terjadi Kesalahan!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponsePetani>, t: Throwable) {
                    Toast.makeText(activity, "Gagal, Terjadi Kesalahan!", Toast.LENGTH_SHORT).show()
                }

            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK) {
//            val uri: Uri = data?.data!!
//            if (requestCode == IMAGE_REQ_CODE) {
//                binding.imgDokumentasi.setImageURI(null)
//                binding.imgDokumentasi.setImageURI(uri)
//                startUploadPhoto(uri)
//            }
//        }
    }

    private fun startUploadPhoto(uri: Uri) {

        val dialogProgress = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        val file = File(uri.path)
        val foto = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val foto_send = MultipartBody.Part.createFormData("foto", file.name, foto)
        val petani_id_send = RequestBody.create("text/plain".toMediaTypeOrNull(), petani_id)

        ApiClient.instances.updatePhotoDokumentasiSensus(petani_id_send, foto_send)
            ?.enqueue(object : Callback<Responses.ResponseSensus> {
                override fun onResponse(
                    call: Call<Responses.ResponseSensus>,
                    response: Response<Responses.ResponseSensus>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            laodData(petani_id)
                            Toast.makeText(
                                activity,
                                "Success upload photo!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            Toast.makeText(activity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(activity, pesanRespon, Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseSensus>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_SHORT)
                        .show()

                    dialogProgress.dismiss()
                }

            })

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