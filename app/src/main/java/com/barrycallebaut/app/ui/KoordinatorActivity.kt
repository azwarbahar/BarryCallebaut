package com.barrycallebaut.app.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.barrycallebaut.app.R
import com.barrycallebaut.app.adapter.PetaniAdapter
import com.barrycallebaut.app.adapter.PetugasAdapter
import com.barrycallebaut.app.database.lokal.PreferencesHelper
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityKoordinatorBinding
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

class KoordinatorActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityKoordinatorBinding

    private lateinit var petani: List<Petani>
    private lateinit var petugas: List<Karyawan>

    private lateinit var sharedPref: PreferencesHelper
    private var petugas_id: String = ""
    private var role: String = ""

    private lateinit var karyawan: Karyawan

    private lateinit var petaniAdapter: PetaniAdapter
    private lateinit var petugasAdapter: PetugasAdapter

    private lateinit var swipe_refresh: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKoordinatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        petugas_id = sharedPref.getString(Constant.ID_USER).toString()
        role = sharedPref.getString(Constant.ROLE).toString()

        binding.imgSearch.setOnClickListener {
            val intent = Intent(this, PencarianPetaniActivity::class.java)
            startActivity(intent)
        }

        binding.cvPhoto.setOnClickListener {
            val intent = Intent(this, AkunPetugasActivity::class.java)
            startActivity(intent)
        }

        binding.tvNama.setOnClickListener {
            val intent = Intent(this, AkunPetugasActivity::class.java)
            startActivity(intent)
        }

        binding.rlBtnPetani.setOnClickListener {
            petaniShow()
        }

        binding.rlBtnPetugas.setOnClickListener {
            petugasShow()
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
            loadDataList(role)
            loadDataSensus()
            loadDataListPetugas()
            loadDataPetugas(petugas_id)
        })

        binding.rvPetani.visibility = View.GONE
        binding.rvPetugas.visibility = View.VISIBLE

    }

    private fun loadDataPetugas(petugasId: String) {

        ApiClient.instances.getKaryawanId(petugasId)
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
                            karyawan = response.body()?.result_karyawan!!
                            initDataPetugas(karyawan)
                        }
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseKaryawan>, t: Throwable) {

                }

            })
    }

    private fun initDataPetugas(karyawan: Karyawan) {

        binding.tvNama.setText(karyawan.nama.toString())
        binding.tvAlamat.setText(karyawan.alamat.toString())

        Glide.with(this)
            .load(Constant.URL_PHOTO + karyawan.foto.toString())
            .into(binding.imgPhoto)


    }

    private fun loadDataSensus() {

        ApiClient.instances.getJumlahSensus(role, petugas_id)
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

    private fun loadDataList(role: String) {

        if (role.equals("Petugas")) {
            ApiClient.instances.getPetaniPetugasId(petugas_id)
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
                                    LinearLayoutManager(this@KoordinatorActivity)
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

        } else {
            ApiClient.instances.getPetani()
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
                                    LinearLayoutManager(this@KoordinatorActivity)
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

    }

    private fun loadDataListPetugas() {

        ApiClient.instances.getKaryawanPosisi("Petugas")
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
                            petugas = response.body()?.karyawan_data!!

                            val rv_petugas = binding.rvPetugas
                            rv_petugas.layoutManager = LinearLayoutManager(this@KoordinatorActivity)
                            petugasAdapter = PetugasAdapter(petugas)
                            rv_petugas.adapter = petugasAdapter
                        }
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseKaryawan>, t: Throwable) {
                    swipe_refresh.isRefreshing = false
                    Log.e("ERROR", "Pesan : " + t.message)
                }

            })


    }

    private fun petaniShow() {

        binding.rvPetani.visibility = View.VISIBLE
        binding.rvPetugas.visibility = View.GONE

        binding.tvBtnPetani.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.rlBtnPetani.background =
            ContextCompat.getDrawable(this, R.drawable.bg_primary_corner_8)

        binding.tvBtnPetugas.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.rlBtnPetugas.background =
            ContextCompat.getDrawable(this, R.drawable.bg_stroke_primary)

    }

    private fun petugasShow() {

        binding.rvPetugas.visibility = View.VISIBLE
        binding.rvPetani.visibility = View.GONE

        binding.tvBtnPetugas.setTextColor(ContextCompat.getColor(this, R.color.white))
        binding.rlBtnPetugas.background =
            ContextCompat.getDrawable(this, R.drawable.bg_primary_corner_8)

        binding.tvBtnPetani.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        binding.rlBtnPetani.background =
            ContextCompat.getDrawable(this, R.drawable.bg_stroke_primary)

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

    override fun onRefresh() {
        loadDataList(role)
        loadDataSensus()
        loadDataListPetugas()
        loadDataPetugas(petugas_id)
    }
}