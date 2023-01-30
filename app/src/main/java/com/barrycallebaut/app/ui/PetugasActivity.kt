package com.barrycallebaut.app.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.barrycallebaut.app.adapter.PetaniAdapter
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityPetugasBinding
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetugasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetugasBinding

    private lateinit var petani: List<Petani>

    private lateinit var petaniAdapter: PetaniAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPetugasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cvPhoto.setOnClickListener {
            val intent = Intent(this, AkunPetugasActivity::class.java)
            startActivity(intent)
        }

        binding.tvNama.setOnClickListener {
            val intent = Intent(this, AkunPetugasActivity::class.java)
            startActivity(intent)
        }

        initChart()
        loadData()

    }

    private fun loadData() {

//        ApiClient.instances.getPetani()?.enqueue(object : Callback<Responses.ResponsePetani> {
//            override fun onResponse(
//                call: Call<Responses.ResponsePetani>,
//                response: Response<Responses.ResponsePetani>
//            ) {
//                if (response.isSuccessful) {
//                    val pesanRespon = response.message()
//                    val message = response.body()?.pesan
//                    val kode = response.body()?.kode
//                    petani = response.body()?.petani_data!!

        val rv_petani = binding.rvPetani
        rv_petani.layoutManager = LinearLayoutManager(this@PetugasActivity)
        petaniAdapter = PetaniAdapter()
        rv_petani.adapter = petaniAdapter

//                }
//
//            }
//
//            override fun onFailure(call: Call<Responses.ResponsePetani>, t: Throwable) {
//                Log.e("ERROR", "Pesan : " + t.message)
//            }
//
//        })


    }

    private fun initChart() {

        var pieChart = binding.pieChart

// Tambahkan entri data ke chart
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(100f))
//        entries.add(PieEntry(20f))
//        entries.add(PieEntry(20f))
//        entries.add(PieEntry(20f))
//        entries.add(PieEntry(20f))
//        entries.add(PieEntry(20f))
//        entries.add(PieEntry(20f))
//        entries.add(PieEntry(20f))
//        entries.add(PieEntry(20f))
//        entries.add(PieEntry(20f))

// Buat dataset dari entri data
        val dataSet = PieDataSet(entries, "sass")

        dataSet.setDrawValues(false)

//        dataSet.sliceSpace = 5f
        dataSet.colors = listOf(
//            Color.RED,
//            Color.RED,
//            Color.RED,
//            Color.RED,
//            Color.RED,
//            Color.BLUE,
//            Color.BLUE,
//            Color.BLUE,
//            Color.BLUE,
            Color.BLUE
        )
// Buat objek PieData dari dataset
        val data = PieData(dataSet)

// Tambahkan data ke chart
        pieChart.data = data
        pieChart.legend.isEnabled = false
//        pieChart.setDrawCenterText(false)

// Update chart
        pieChart.invalidate()
//        dataSet.isDrawLabelsEnabled  = false

    }
}