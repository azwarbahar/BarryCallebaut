package com.barrycallebaut.app.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barrycallebaut.app.R
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ItemPetaniBinding
import com.barrycallebaut.app.databinding.ItemPetugasBinding
import com.barrycallebaut.app.models.Inspeksi
import com.barrycallebaut.app.models.Karyawan
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.ui.DetailPetugasActivity
import com.barrycallebaut.app.ui.PetaniActivity
import com.barrycallebaut.app.utils.Constant
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetugasAdapter(private var list: List<Karyawan>) :
    RecyclerView.Adapter<PetugasAdapter.MyHolderView>() {


    class MyHolderView(private var binding: ItemPetugasBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var inspeksi: List<Inspeksi>
        fun bind(get: Karyawan) {
            with(itemView) {

                Glide.with(context)
                    .load(Constant.URL_PHOTO + get.foto.toString())
                    .into(binding.imgPhoto)

                var nama = get.nama.toString()
//                var status = get.status.toString()

                if (nama.equals("") || nama.equals("null")) {
                    binding.tvNama.setText("Name this")
                } else {
                    binding.tvNama.setText(nama)
                }
                loadDataSensus("Petugas", get.id.toString())
//                loadData(get.id.toString(), status)

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailPetugasActivity::class.java)
                    intent.putExtra("petugas", get)
                    context.startActivity(intent)

                }

            }
        }

        private fun loadDataSensus(role: String, petugas_id: String) {

            ApiClient.instances.getJumlahSensus(role, petugas_id)
                ?.enqueue(object : Callback<Responses.ResponseJumlahSensus> {
                    override fun onResponse(
                        call: Call<Responses.ResponseJumlahSensus>,
                        response: Response<Responses.ResponseJumlahSensus>
                    ) {

                        if (response.isSuccessful) {
                            val pesanRespon = response.message()
                            val message = response.body()?.pesan
                            val kode = response.body()?.kode
                            if (kode.equals("1")) {
                                var sudah_sensus = response.body()?.sudah_sensus.toString()
                                var belum_sensus = response.body()?.belum_sensus.toString()
                                var suspended = response.body()?.suspended.toString()
                                var total = sudah_sensus.toInt() + belum_sensus.toInt()
                                binding.tvStatus.setText(sudah_sensus + "/" + total.toString())
                            }
                        }

                    }

                    override fun onFailure(
                        call: Call<Responses.ResponseJumlahSensus>,
                        t: Throwable
                    ) {

                        Log.e("ERROR", "Pesan : " + t.message)
                    }

                })


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemPetugasBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size


    fun filterList(listt: List<Karyawan>) {
        list = listt
        notifyDataSetChanged()
    }

}