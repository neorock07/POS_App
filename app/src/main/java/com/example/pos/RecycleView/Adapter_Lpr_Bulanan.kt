package com.example.pos.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Activity.MainActivity
import com.example.pos.Model.model_tahunan
import com.example.pos.R

class Adapter_Lpr_Bulanan(private val context: Context, val list_data:ArrayList<model_tahunan>?):
    RecyclerView.Adapter<Adapter_Lpr_Bulanan.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val v:View = LayoutInflater.from(parent.context).inflate(R.layout.container_tahunan, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val main:MainActivity = MainActivity()
        holder.bulan.text = list_data!!.get(position).tahun
        holder.income.text = "Rp " + main.NumberFormat(list_data!!.get(position).income.toString())
    }

    override fun getItemCount(): Int {
        return list_data!!.size
    }

    class Holder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val bulan = itemView.findViewById<TextView>(R.id.txt_bulan)
        val income = itemView.findViewById<TextView>(R.id.total_pendapatan_tahun)
    }

}