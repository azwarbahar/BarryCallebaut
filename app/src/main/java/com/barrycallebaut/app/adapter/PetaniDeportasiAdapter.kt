package com.barrycallebaut.app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barrycallebaut.app.R
import com.barrycallebaut.app.database.server.ApiClient
import com.barrycallebaut.app.databinding.ItemPetaniBinding
import com.barrycallebaut.app.databinding.ItemPetaniDeportasiBinding
import com.barrycallebaut.app.models.Inspeksi
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.models.Responses
import com.barrycallebaut.app.ui.PetaniActivity
import com.barrycallebaut.app.utils.Constant
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetaniDeportasiAdapter(private var list: List<Petani>) :
    RecyclerView.Adapter<PetaniDeportasiAdapter.MyHolderView>() {


    class MyHolderView(private var binding: ItemPetaniDeportasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var inspeksi: List<Inspeksi>
        fun bind(get: Petani) {
            with(itemView) {

                Glide.with(context)
                    .load(Constant.URL_PHOTO + get.foto.toString())
                    .into(binding.imgPhoto)

                var nama = get.nama.toString()
                var alamat = get.alamat.toString()
                var status = get.status.toString()

                if (nama.equals("") || nama.equals("null")) {
                    binding.tvNama.setText("Name this")
                } else {
                    binding.tvNama.setText(nama)
                }

                if (alamat.equals("") || alamat.equals("null")) {
                    var kelurahan = get.kelurahan.toString()
                    var kecamatan = get.kecamatan.toString()
                    binding.tvAlamat.setText(kelurahan + ", " + kecamatan)
                } else {
                    binding.tvAlamat.setText(alamat)
                }

//                loadData(get.id.toString(), status)

                itemView.setOnClickListener {
                    val intent = Intent(context, PetaniActivity::class.java)
                    intent.putExtra("petani", get)
                    context.startActivity(intent)

                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemPetaniDeportasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size


    fun filterList(listt: List<Petani>) {
        list = listt
        notifyDataSetChanged()
    }

}