package com.barrycallebaut.app.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.barrycallebaut.app.R
import com.barrycallebaut.app.adapter.PetaniAdapter
import com.barrycallebaut.app.adapter.PetaniDeportasiAdapter
import com.barrycallebaut.app.database.lokal.PreferencesHelper
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityDeportasiPetaniBinding
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeportasiPetaniActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityDeportasiPetaniBinding

    private lateinit var petani: List<Petani>

    private lateinit var sharedPref: PreferencesHelper
    private var petugas_id: String = ""
    private var role: String = ""

    private lateinit var petaniDeportasiAdapter: PetaniDeportasiAdapter

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeportasiPetaniBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        petugas_id = sharedPref.getString(Constant.ID_USER).toString()
        role = sharedPref.getString(Constant.ROLE).toString()

        binding.imgBack.setOnClickListener { finish() }

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
        })

    }

    private fun loadDataList(role: String) {

        ApiClient.instances.getPetaniDeportasi(petugas_id, role)
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

                            if (petani.size > 0) {
                                binding.imgEmpty.visibility = View.GONE
                                binding.rvPetani.visibility = View.VISIBLE

                                val rv_petani = binding.rvPetani
                                petaniDeportasiAdapter = PetaniDeportasiAdapter(petani)
                                rv_petani.layoutManager =
                                    LinearLayoutManager(this@DeportasiPetaniActivity)
                                rv_petani.adapter = petaniDeportasiAdapter
                            } else {
                                binding.imgEmpty.visibility = View.VISIBLE
                                binding.rvPetani.visibility = View.GONE
                            }
                        } else {
                            binding.imgEmpty.visibility = View.VISIBLE
                            binding.rvPetani.visibility = View.GONE
                        }
                    } else {
                        binding.imgEmpty.visibility = View.VISIBLE
                        binding.rvPetani.visibility = View.GONE
                    }

                }

                override fun onFailure(call: Call<Responses.ResponsePetani>, t: Throwable) {
                    swipe_refresh.isRefreshing = false

                    binding.imgEmpty.visibility = View.VISIBLE
                    binding.rvPetani.visibility = View.GONE

                }

            })


    }

    override fun onRefresh() {
        loadDataList(role)
    }
}