package com.barrycallebaut.app.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.barrycallebaut.app.R
import com.barrycallebaut.app.adapter.PetaniAdapter
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityDetailPetugasBinding
import com.barrycallebaut.app.models.Karyawan
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.utils.Constant
import com.bumptech.glide.Glide
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPetugasActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityDetailPetugasBinding

    private lateinit var petani: List<Petani>
    private lateinit var petugas: Karyawan

    private var petugas_id: String = ""

    private lateinit var swipe_refresh: SwipeRefreshLayout

    private lateinit var petaniAdapter: PetaniAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPetugasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        petugas = intent.getParcelableExtra("petugas")!!
        swipe_refresh = binding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )

        initData(petugas)

        binding.continerPetani.visibility = View.GONE
        binding.continerData.visibility = View.VISIBLE

        binding.rlBtnPetani.setOnClickListener {
            petaniShow()
        }

        binding.rlBtnData.setOnClickListener {
            dataShow()
        }

        binding.imgBack.setOnClickListener { finish() }

        binding.imgPhoto.setOnClickListener {

            var foto_intent = Constant.URL_PHOTO + petugas.foto.toString()
            val intent = Intent(this, PreviewPhotoActivity::class.java)
            intent.putExtra("foto", foto_intent)
            startActivity(intent)
        }

    }

    private fun loadDataSensus() {

        ApiClient.instances.getJumlahSensus("Petugas", petugas_id)
            ?.enqueue(object : Callback<Responses.ResponseJumlahSensus> {
                override fun onResponse(
                    call: Call<Responses.ResponseJumlahSensus>,
                    response: Response<Responses.ResponseJumlahSensus>
                ) {
                    swipe_refresh.isRefreshing = false
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            var sudah_sensus = response.body()?.sudah_sensus.toString()
                            var belum_sensus = response.body()?.belum_sensus.toString()
                            var suspended = response.body()?.suspended.toString()
                            initChart(sudah_sensus, belum_sensus)
                            binding.tvTidakCapai.setText(suspended)
                        }
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseJumlahSensus>, t: Throwable) {
                    swipe_refresh.isRefreshing = false
                    Log.e("ERROR", "Pesan : " + t.message)
                }

            })


    }

    private fun initChart(sudah: String, belum: String) {

        binding.tvSudahSensus.setText(sudah)
        binding.tvBelumSensus.setText(belum)

        var total_data = sudah.toDouble() + belum.toDouble()
        var total_data_int = sudah.toInt() + belum.toInt()

        var sudah_for_text = (sudah.toDouble() / total_data * 100).toInt()
        var sudah_for_chart: Float = (sudah.toDouble() / total_data * 100).toFloat()
        var belum_for_chart: Float = (belum.toDouble() / total_data * 100).toFloat()

//        Toast.makeText(this, ""+sudah_for_chart, Toast.LENGTH_SHORT).show()

        var persen_sudah = sudah.toDouble() / total_data * 100
        binding.tvPersenSensus.setText("" + sudah_for_text + "%")

        var pieChart = binding.pieChart

// Tambahkan entri data ke chart
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(sudah_for_chart))
        entries.add(PieEntry(belum_for_chart))
// Buat dataset dari entri data
        val dataSet = PieDataSet(entries, "sass")

        dataSet.setDrawValues(false)

//        dataSet.sliceSpace = 5f
        dataSet.colors = listOf(
            Color.parseColor("#FFBA7D"),
            Color.parseColor("#FFFFFF")
        )
// Buat objek PieData dari dataset
        val data = PieData(dataSet)

// Tambahkan data ke chart
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
//        pieChart.setDrawCenterText(false)

        pieChart.setBackgroundColor(Color.TRANSPARENT)

        pieChart.holeRadius = 70f
        pieChart.transparentCircleRadius = 0f
        pieChart.setTransparentCircleColor(Color.parseColor("#972727"))

// Update chart
        pieChart.invalidate()
//        dataSet.isDrawLabelsEnabled  = false

    }

    private fun dataShow() {
        binding.continerPetani.visibility = View.GONE
        binding.continerData.visibility = View.VISIBLE

        binding.tvBtnData.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.rlBtnData.background =
            ContextCompat.getDrawable(this, R.drawable.bg_primary_corner_8)

        binding.tvBtnPetani.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.rlBtnPetani.background =
            ContextCompat.getDrawable(this, R.drawable.bg_stroke_primary)


    }

    private fun petaniShow() {

        binding.continerPetani.visibility = View.VISIBLE
        binding.continerData.visibility = View.GONE

        binding.tvBtnPetani.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.rlBtnPetani.background =
            ContextCompat.getDrawable(this, R.drawable.bg_primary_corner_8)

        binding.tvBtnData.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.rlBtnData.background =
            ContextCompat.getDrawable(this, R.drawable.bg_stroke_primary)

    }

    private fun initData(petugas: Karyawan) {
        petugas_id = petugas.id.toString()
        binding.tvNama.setText(petugas.nama.toString())
        binding.tvJekel.setText(petugas.jenis_kelamin.toString())
        binding.tvPosisi.setText(petugas.posisi.toString())
        binding.tvId.setText(petugas.no_kepegawaian.toString())
        binding.tvTanggalLahir.setText(Constant.formatDate(petugas.tanggal_lahir.toString()))
        binding.tvKontak.setText(petugas.kontak.toString())
        binding.tvAlamat.setText(petugas.alamat.toString())

        Glide.with(this)
            .load(Constant.URL_PHOTO + petugas.foto.toString())
            .into(binding.imgPhoto)

        loadDataList(petugas_id)
        loadDataSensus()

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
                            petugas = response.body()?.result_karyawan!!
                            initData(petugas)
                        }
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseKaryawan>, t: Throwable) {
                    swipe_refresh.isRefreshing = false
                }

            })

    }

    private fun loadDataList(petugasId: String) {
        ApiClient.instances.getPetaniPetugasId(petugasId)
            ?.enqueue(object : Callback<Responses.ResponsePetani> {
                override fun onResponse(
                    call: Call<Responses.ResponsePetani>,
                    response: Response<Responses.ResponsePetani>
                ) {
                    swipe_refresh.isRefreshing = false
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            petani = response.body()?.petani_data!!

                            val rv_petani = binding.rvPetani
                            rv_petani.layoutManager =
                                LinearLayoutManager(this@DetailPetugasActivity)
                            petaniAdapter = PetaniAdapter(petani)
                            rv_petani.adapter = petaniAdapter
                        }
                    }

                }

                override fun onFailure(call: Call<Responses.ResponsePetani>, t: Throwable) {
                    swipe_refresh.isRefreshing = false
                    Log.e("ERROR", "Pesan : " + t.message)
                }

            })
    }

    override fun onRefresh() {
        loadData(petugas_id)
        loadDataList(petugas_id)
    }
}