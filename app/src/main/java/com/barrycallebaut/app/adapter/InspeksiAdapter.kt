package com.barrycallebaut.app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barrycallebaut.app.databinding.ItemInspeksiBinding
import com.barrycallebaut.app.models.Inspeksi
import com.barrycallebaut.app.ui.DetailInspeksiActivity
import com.barrycallebaut.app.ui.PetaniActivity
import com.barrycallebaut.app.utils.Constant

class InspeksiAdapter(private var list: List<Inspeksi>) :
    RecyclerView.Adapter<InspeksiAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemInspeksiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Inspeksi, position: Int) {
            with(itemView) {

                var tanggal = get.tanggal.toString()
                binding.tvTanggal.setText(Constant.formatDate(tanggal))

                var nomor = position + 1
                binding.tvTitle.setText("Inspeksi " + nomor)

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailInspeksiActivity::class.java)
                    intent.putExtra("inspeksi", get)
                    intent.putExtra("posisi", nomor)
                    context.startActivity(intent)

                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemInspeksiBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position), position)

    override fun getItemCount() = list.size
}