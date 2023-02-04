package com.barrycallebaut.app.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.barrycallebaut.app.adapter.InspeksiAdapter
import com.barrycallebaut.app.database.lokal.PreferencesHelper
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.FragmentInspeksiBinding
import com.barrycallebaut.app.models.Inspeksi
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.ui.TambahInspeksiActivity
import com.barrycallebaut.app.utils.Constant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InspeksiFragment : Fragment() {
    private var _binding: FragmentInspeksiBinding? = null
    private val binding get() = _binding!!

    private lateinit var inspeksiAdapter: InspeksiAdapter
    private lateinit var inspeksi: List<Inspeksi>
    private lateinit var petani: Petani
    private lateinit var sharedPref: PreferencesHelper
    private var petani_id: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInspeksiBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPref = PreferencesHelper(context)
        petani_id = sharedPref.getString(Constant.ID_PETANI_SELECTED).toString()

        binding.llTambah.setOnClickListener {
            val intent = Intent(context, TambahInspeksiActivity::class.java)
            startActivity(intent)
        }

        loadData(petani_id)

        return view
    }

    private fun loadData(petani_id: String) {

        ApiClient.instances.getInspeksiPetaniId(petani_id)
            ?.enqueue(object : Callback<Responses.ResponseInspeksi> {
                override fun onResponse(
                    call: Call<Responses.ResponseInspeksi>,
                    response: Response<Responses.ResponseInspeksi>
                ) {
                    if (response.isSuccessful) {
                        var kode = response.body()?.kode
                        var pesan = response.body()?.pesan
                        var message = response.message()
                        if (kode.equals("1")) {
                            inspeksi = response.body()?.inspeksi_data!!
                            initInspeksi(inspeksi)

                        } else {

                        }


                    } else {

                    }

                }

                override fun onFailure(call: Call<Responses.ResponseInspeksi>, t: Throwable) {

                }

            })

    }

    private fun initInspeksi(inspeksi: List<Inspeksi>) {

        var jumlah_data = inspeksi.size
        if (jumlah_data > 4) {
            binding.tvSkala.setText("4/4")
        } else {
            binding.tvSkala.setText("" + jumlah_data + "/4")
        }

        val rv_inspeksi = binding.rvInspeksi
        rv_inspeksi.layoutManager = LinearLayoutManager(activity)
        inspeksiAdapter = InspeksiAdapter(inspeksi)
        rv_inspeksi.adapter = inspeksiAdapter

    }
}