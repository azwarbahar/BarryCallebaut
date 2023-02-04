package com.barrycallebaut.app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barrycallebaut.app.R
import com.barrycallebaut.app.databinding.ItemPetaniBinding
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.ui.PetaniActivity
import com.barrycallebaut.app.utils.Constant
import com.bumptech.glide.Glide

class PetaniAdapter(private var list: List<Petani>) :
    RecyclerView.Adapter<PetaniAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemPetaniBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
                    binding.tvAlamat.setText("Alamat this")
                } else {
                    binding.tvAlamat.setText(alamat)
                }

                if (status.equals("Aktif")) {
                    Glide.with(this)
                        .load(R.drawable.ic_baseline_check_circle_24)
                        .into(binding.imgStatus)
                } else if (status.equals("Suspended")) {
                    Glide.with(this)
                        .load(R.drawable.ic_baseline_access_time_24)
                        .into(binding.imgStatus)
                } else {
                    Glide.with(this)
                        .load(R.drawable.ic_baseline_check_circle_24)
                        .into(binding.imgStatus)
                }

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
            ItemPetaniBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}