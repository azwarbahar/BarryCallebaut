package com.barrycallebaut.app.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog
import com.barrycallebaut.app.adapter.TabAdapter
import com.barrycallebaut.app.database.lokal.PreferencesHelper
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityPetaniBinding
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.utils.Constant
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PetaniActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetaniBinding

    private lateinit var sharedPref: PreferencesHelper

    private lateinit var petani: Petani

    private var petani_id: String = ""

    private val PROFILE_IMAGE_REQ_CODE = 101
    private val IMAGE_REQ_CODE = 102

    val tabArray = arrayOf(
        "Sensus",
        "Inspeksi"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetaniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        petani = intent.getParcelableExtra("petani")!!
        setupShared(petani.id.toString())
        initData(petani)
        setupTabFragment()

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
                    1 -> openPreviewImage("Profil")
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

    }

    private fun openPreviewImage(s: String) {

//        var foto = user.foto
//        var header = user.foto_sampul
//        if (s.equals("Profil")) {
//            var foto_intent = BuildConfig.BASE_URL + "upload/photo/" + foto
//
//            val intent = Intent(context, ShowPhotoActivity::class.java)
//            intent.putExtra("foto", foto_intent)
//            startActivity(intent)
//        } else {
//            var foto_intent = BuildConfig.BASE_URL + "upload/photo/" + header
//
//            val intent = Intent(context, ShowPhotoActivity::class.java)
//            intent.putExtra("foto", foto_intent)
//            startActivity(intent)
//        }

    }

    private fun openImagePicker(s: Int) {
        ImagePicker.with(this)
            .cropSquare()
            .compress(2048)
            .maxResultSize(1000, 1000)
            .start(PROFILE_IMAGE_REQ_CODE)

    }

    private fun initData(petani: Petani) {
        petani_id = petani.id.toString()
        Glide.with(this)
            .load(Constant.URL_PHOTO + petani.foto.toString())
            .into(binding.imgPhoto)

        var nama = petani.nama.toString()
        var kelurahan = petani.kelurahan.toString()
        var kecamatan = petani.kecamatan.toString()
        var kontak = petani.kontak.toString()

        binding.tvNama.setText(nama)
        binding.tvSubtitle.setText(kelurahan + ", " + kecamatan)
        binding.tvKontak.setText(kontak)

        binding.imgCopyKontak.setOnClickListener {

            val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("text", kontak)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            if (requestCode == PROFILE_IMAGE_REQ_CODE) {
                binding.imgPhoto.setImageURI(null)
                binding.imgPhoto.setImageURI(uri)
                startUploadPhoto(uri)
            } else if (requestCode == IMAGE_REQ_CODE) {
                startUploadPhotoFragment(uri)
            }
        }
    }

    private fun startUploadPhotoFragment(uri: Uri) {

        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
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
                            loadData(petani_id)
                            setupTabFragment()
                            Toast.makeText(
                                this@PetaniActivity,
                                "Success upload photo!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            Toast.makeText(this@PetaniActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@PetaniActivity, pesanRespon, Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseSensus>, t: Throwable) {
                    Toast.makeText(this@PetaniActivity, t.message, Toast.LENGTH_SHORT)
                        .show()

                    dialogProgress.dismiss()
                }

            })

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
        val user_id = RequestBody.create("text/plain".toMediaTypeOrNull(), petani_id)

        ApiClient.instances.updatePhotoPetani(user_id, foto_send)
            ?.enqueue(object : Callback<Responses.ResponsePetani> {
                override fun onResponse(
                    call: Call<Responses.ResponsePetani>,
                    response: Response<Responses.ResponsePetani>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            loadData(petani_id)
                            Toast.makeText(
                                this@PetaniActivity,
                                "Success upload photo!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            Toast.makeText(this@PetaniActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@PetaniActivity, pesanRespon, Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onFailure(call: Call<Responses.ResponsePetani>, t: Throwable) {
                    Toast.makeText(this@PetaniActivity, t.message, Toast.LENGTH_SHORT)
                        .show()

                    dialogProgress.dismiss()
                }

            })

    }

    private fun loadData(petaniId: String) {

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


    private fun setupShared(id: String) {
        sharedPref.put(Constant.ID_PETANI_SELECTED, id)
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