package com.example.pos.RecycleView

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Activity.Laporan_Bulanan
import com.example.pos.Activity.Laporan_Tahunan
import com.example.pos.Activity.MainActivity
import com.example.pos.Model.model_tahunan
import com.example.pos.R
import org.w3c.dom.Text

class Adapter_Lpr_Tahunan(private val context:Context,private var list_data:ArrayList<model_tahunan>?):
    RecyclerView.Adapter<Adapter_Lpr_Tahunan.MyHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v:View = LayoutInflater.from(parent.context).inflate(R.layout.container_tahunan,parent,false)
        return MyHolder(v)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val main:MainActivity = MainActivity()
        holder.txt_tahun.text = list_data!!.get(position).tahun
        holder.txt_income.text = "Rp "+ main.NumberFormat(list_data!!.get(position).income.toString())
        //button click
        holder.card.setOnClickListener{
            val log:Intent = Intent(context, Laporan_Bulanan::class.java)
            log.putExtra("Key_Tahun", list_data!!.get(position).tahun)
            startActivity(context,log, null)
            Toast.makeText(context,"data : ${list_data!!.get(position).tahun}",Toast.LENGTH_LONG).show()
        }

    }

    override fun getItemCount(): Int {
        return list_data!!.size
    }

    class MyHolder(val itemView: View):RecyclerView.ViewHolder(itemView) {
        val txt_tahun = itemView.findViewById<TextView>(R.id.txt_tahun)
        val txt_income = itemView.findViewById<TextView>(R.id.total_pendapatan_tahun)
        val card = itemView.findViewById<CardView>(R.id.card_thn)

    }
}