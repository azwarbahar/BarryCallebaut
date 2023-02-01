package com.barrycallebaut.app.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.barrycallebaut.app.databinding.ItemInspeksiBinding
import com.barrycallebaut.app.databinding.ItemPetaniBinding
import com.barrycallebaut.app.models.Petani
import com.barrycallebaut.app.ui.AkunPetugasActivity
import com.barrycallebaut.app.ui.PetaniActivity
import kotlinx.android.synthetic.main.activity_petani.view.*

class InspeksiAdapter() :
    RecyclerView.Adapter<InspeksiAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemInspeksiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(itemView) {

                itemView.setOnClickListener {
                    val intent = Intent(context, PetaniActivity::class.java)
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
        holder.bind()

    override fun getItemCount() = 2
}