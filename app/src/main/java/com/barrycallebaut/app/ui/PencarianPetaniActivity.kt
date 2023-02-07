package com.barrycallebaut.app.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.barrycallebaut.app.R
import com.barrycallebaut.app.adapter.PetaniAdapter
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ActivityPencarianPetaniBinding
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class PencarianPetaniActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityPencarianPetaniBinding

    private lateinit var petani: List<Petani>

    private var petugas_id: String = "7"
    private lateinit var petaniAdapter: PetaniAdapter

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPencarianPetaniBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipe_refresh = binding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_refresh.post(Runnable {
            loadData()
        })


        binding.etCari.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().isEmpty() || editable.toString() == "") {
                    petaniAdapter.filterList(petani)
                } else {
                    filter(editable.toString())
                }
            }
        })

        binding.imgBack.setOnClickListener { finish() }

    }

    private fun filter(text: String) {
        val filteredList: ArrayList<Petani> = ArrayList()
        for (item in petani) {
            var nama = item.nama.toString()
            var kontak = item.kontak.toString()
            var id_petani = item.id_petani.toString()
            if (nama.toLowerCase().contains(text.lowercase(Locale.getDefault())) ||
                kontak.toLowerCase().contains(text.lowercase(Locale.getDefault())) ||
                id_petani.toLowerCase().contains(text.lowercase(Locale.getDefault()))
            ) {
                filteredList.add(item)
            }
        }
        petaniAdapter.filterList(filteredList)
        if (filteredList.size > 0) {
            binding.imgEmpty.visibility = View.GONE
        } else {
            binding.imgEmpty.visibility = View.VISIBLE
        }

    }

    private fun loadData() {

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

                            if (petani.size > 0) {

                                binding.rvPetani.visibility = View.VISIBLE
                                binding.imgEmpty.visibility = View.GONE
                                val rv_petani = binding.rvPetani
                                rv_petani.layoutManager =
                                    LinearLayoutManager(this@PencarianPetaniActivity)
                                petaniAdapter = PetaniAdapter(petani)
                                rv_petani.adapter = petaniAdapter

                            } else {
                                binding.rvPetani.visibility = View.GONE
                                binding.imgEmpty.visibility = View.VISIBLE
                            }
                        } else {
                            binding.rvPetani.visibility = View.GONE
                            binding.imgEmpty.visibility = View.VISIBLE
                        }
                    } else {
                        binding.rvPetani.visibility = View.GONE
                        binding.imgEmpty.visibility = View.VISIBLE
                    }

                }

                override fun onFailure(call: Call<Responses.ResponsePetani>, t: Throwable) {
                    swipe_refresh.isRefreshing = false
                    Log.e("ERROR", "Pesan : " + t.message)
                    binding.rvPetani.visibility = View.GONE
                    binding.imgEmpty.visibility = View.VISIBLE
                }

            })


    }

    override fun onRefresh() {
        loadData()
    }

}