package com.example.pos.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Model.model_barang
import com.example.pos.R

class Adapter_pembayaran(
    private var context: Context,
    private var list_kode:ArrayList<String>?,
    private var list_nama:ArrayList<String>?,
    private var list_harga:ArrayList<Int>?,
    private var list_jenis:ArrayList<String>?,
    private var list_jumlah:ArrayList<Int>?
    ): RecyclerView.Adapter<Adapter_pembayaran.MyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v:View = LayoutInflater.from(parent.context).inflate(R.layout.container_item_beli, parent, false)
        return MyHolder(v)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
//        var model = model_item.get(position)
//        holder.namaBarang.text = model.nama
//        holder.hargaBarang.text = "Rp." + NumberFormat(model.harga.toString())
//        holder.jenisItem.text = model.jenis
        holder.jumlah.text = list_jumlah!!.get(position).toString()
        // sets the image to the imageview from our itemHolder class
        holder.jenisItem.text = list_jenis!!.get(position)
        // sets the text to the textview from our itemHolder class
        holder.namaBarang.text = list_nama!!.get(position)
        val hargaString = "Rp"+ NumberFormat(list_harga!!.get(position).toString())

    }
    fun NumberFormat(s:String):String{
        var current:String = ""
        var parsed:Double
        var cleanString:String = s.replace("""[,.]""".toRegex(), "")
        parsed = cleanString.toDouble()
        var formatted:String = java.text.NumberFormat.getNumberInstance().format(parsed)
        current = formatted
        return current
    }

    override fun getItemCount(): Int {
        return list_kode!!.size
    }

    class MyHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val jenisItem: TextView = itemView.findViewById(R.id.jenis_item)
        val namaBarang: TextView = itemView.findViewById(R.id.detail_item)
        val hargaBarang: TextView = itemView.findViewById(R.id.harga_item)
        val plusButton : CardView = itemView.findViewById(R.id.btn_add)
        val minButton : CardView = itemView.findViewById(R.id.btn_min)
        val jumlah: TextView = itemView.findViewById(R.id.jumlah_item)
    }
}