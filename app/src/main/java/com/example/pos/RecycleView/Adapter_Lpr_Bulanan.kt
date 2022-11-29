package com.example.pos.RecycleView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Activity.Laporan_Bulanan
import com.example.pos.Activity.Laporan_Harian
import com.example.pos.Activity.MainActivity
import com.example.pos.Model.model_bulan
import com.example.pos.Model.model_tahunan
import com.example.pos.R

class Adapter_Lpr_Bulanan(private val context: Context, val list_data:ArrayList<model_bulan>?):
    RecyclerView.Adapter<Adapter_Lpr_Bulanan.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v:View = LayoutInflater.from(parent.context).inflate(R.layout.container_tahunan, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val main:MainActivity = MainActivity()
        holder.bulan.text = list_data!!.get(position).bulan
        holder.income.text = "Rp " + main.NumberFormat(list_data!!.get(position).income.toString())
        holder.card.setOnClickListener{
            val log: Intent = Intent(context, Laporan_Harian::class.java)
            log.putExtra("Key_Bulan", list_data!!.get(position).bulan)
            log.putExtra("Key_Tahun", list_data!!.get(position).tahun)
            ContextCompat.startActivity(context, log, null)
        }
    }

    override fun getItemCount(): Int {
        return list_data!!.size
    }

    class Holder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val bulan = itemView.findViewById<TextView>(R.id.txt_tahun)
        val income = itemView.findViewById<TextView>(R.id.total_pendapatan_tahun)
        val card = itemView.findViewById<CardView>(R.id.card_thn)
    }

}