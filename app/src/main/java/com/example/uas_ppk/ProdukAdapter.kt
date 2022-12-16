package com.example.uas_ppk

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_ppk.databinding.ListDataProdukBinding

class ProdukAdapter(
    private val listProduk:ArrayList<ProdukData>
):RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder>() {

    inner class ProdukViewHolder(item:ListDataProdukBinding):RecyclerView.ViewHolder(item.root){
        private val binding = item
        fun bind(produkData: ProdukData){
            with(binding){
                txtNama.text = produkData.nama
                txtHarga.text = produkData.harga
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdukViewHolder {
        return ProdukViewHolder(ListDataProdukBinding.inflate(LayoutInflater.from(parent.context),
        parent,false
        ))
    }

    override fun onBindViewHolder(holder: ProdukViewHolder, position: Int) {
        holder.bind(listProduk[position])
    }

    override fun getItemCount(): Int = listProduk.size

}