package com.example.pos.RecycleView

import android.content.Context
import  android.view.LayoutInflater
import android.view.View
import  android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
import com.example.pos.Model.model_laporan
import com.example.pos.R


class Adapter_Laporan(
    private var context: Context,
//    private var list_id: ArrayList<Int>?,
//    private var list_kode: ArrayList<String>?,
//    private var list_nama: ArrayList<String>?,
//    private var list_jenis: ArrayList<String>?,
//    private var list_harga: ArrayList<Int>?,
//    private var list_stok: ArrayList<Int>?,
//    private var list_total_unit: ArrayList<Int>?,
//    private var list_total_penjualan: ArrayList<Int>?,
//    private var list_tanggal: ArrayList<Int>?
    private var modelItem: ArrayList<model_laporan>
) : RecyclerView.Adapter<Adapter_Laporan.LaporanViewHolder>() {
    private var listener: Adapter_Laporan.OnItemsClickListener? = null
    public val arrayLaporan = Array<String>(itemCount) { "" }
    val db: Database = Database(context)

    class LaporanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idLaporan: TextView = itemView.findViewById(R.id.id_laporan)
        val kodeBarang: TextView = itemView.findViewById(R.id.kode_barang)
        val namaBarang: TextView = itemView.findViewById(R.id.nama_barang)
        val jenisBarang: TextView = itemView.findViewById(R.id.jenis_barang)
        val hargaBarang: TextView = itemView.findViewById(R.id.harga_barang)
        val stokBarang: TextView = itemView.findViewById(R.id.stok_barang)
        val totalunitLaporan: TextView = itemView.findViewById(R.id.totalunit_laporan)
        val totalpenjualanLaporan: TextView = itemView.findViewById(R.id.totalpenjualan_laporan)
        val tanggalLaporan: TextView = itemView.findViewById(R.id.tanggal_laporan)

    }

    interface OnItemsClickListener {
        fun onItemClick(refresh: Boolean)
    }

    fun setWhenClickListener(listener: Adapter_Laporan.OnItemsClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.container_laporan, parent, false)
        return LaporanViewHolder(v)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
//        holder.kodeBarang.text = list_kode!!.get(position)
//        holder.hargaBarang.text = "Rp." + NumberFormat(list_harga!!.get(position).toString())
//        holder.stokBarang.text = list_stok!!.get(position).toString()
//        holder.jenisBarang.text = list_jenis!!.get(position)
//        holder.namaBarang.text = list_nama!!.get(position)
//        holder.tanggalLaporan.text = list_tanggal!!.get(position).toString()
//        holder.totalunitLaporan.text = list_total_unit!!.get(position).toString()
//        holder.totalpenjualanLaporan.text = list_total_penjualan!!.get(position).toString()
        holder.kodeBarang.text = "KODE : " + modelItem.get(position).kode
        holder.hargaBarang.text = "Rp. " + NumberFormat(modelItem.get(position).harga.toString())
        holder.stokBarang.text = "Stok : " + NumberFormat(modelItem.get(position).stok.toString())
        holder.jenisBarang.text = modelItem.get(position).jenis
        holder.namaBarang.text = modelItem.get(position).nama
        holder.tanggalLaporan.text = modelItem.get(position).tanggal.toString()
        holder.totalunitLaporan.text =
            "Unit : " + NumberFormat(modelItem.get(position).unit.toString())
        holder.totalpenjualanLaporan.text =
            "Rp. " + NumberFormat(modelItem.get(position).penjualan.toString())
    }

    fun filterLaporan(filterLaporan: ArrayList<model_laporan>) {
        modelItem = filterLaporan
        notifyDataSetChanged()
    }

    fun NumberFormat(s: String): String {
        var current: String = ""
        var parsed: Double
        var cleanString: String = s.replace(""" [,.] """.toRegex(), "")
        parsed = cleanString.toDouble()
        var formatted: String = java.text.NumberFormat.getNumberInstance().format(parsed)
        current = formatted
        return current
    }

    override fun getItemCount(): Int {
        return modelItem.size
    }
}