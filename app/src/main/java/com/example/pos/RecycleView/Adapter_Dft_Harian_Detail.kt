package com.example.pos.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Model.model_harian
import com.example.pos.Model.model_harian_detail
import com.example.pos.Model.model_tahunan
import com.example.pos.R

class Adapter_Dft_Harian_Detail(private var list_data:ArrayList<model_harian_detail>?):
    RecyclerView.Adapter<Adapter_Dft_Harian_Detail.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_Dft_Harian_Detail.MyHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.container_item_barang_laporan_harian,parent,false)
        return Adapter_Dft_Harian_Detail.MyHolder(v)
    }

    override fun onBindViewHolder(holder: Adapter_Dft_Harian_Detail.MyHolder, position: Int) {
        holder.txt_jumlah.text = list_data!!.get(position).jumlah.toString()
        holder.txt_jenis.text = list_data!!.get(position).jenis
        holder.txt_harga.text = list_data!!.get(position).harga
        holder.txt_nama.text = list_data!!.get(position).nama
    }
    override fun getItemCount(): Int {
        return list_data!!.size
    }
    class MyHolder(val itemView: View):RecyclerView.ViewHolder(itemView) {
        val txt_jumlah = itemView.findViewById<TextView>(R.id.jumlah_item)
        val txt_nama = itemView.findViewById<TextView>(R.id.detail_item)
        val txt_jenis = itemView.findViewById<TextView>(R.id.jenis_item)
        val txt_harga = itemView.findViewById<TextView>(R.id.harga_item)
    }
}