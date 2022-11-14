package com.example.pos.RecycleView

import android.content.Context
import  android.view.LayoutInflater
import android.view.View
import  android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Model.model_laporan
import com.example.pos.R


class Adapter_Laporan (
    private var context: Context,
    private var list_id:ArrayList<Int>?,
    private var list_kode:ArrayList<String>?,
    private var list_nama:ArrayList<String>?,
    private var list_jenis:ArrayList<String>?,
    private var list_harga:ArrayList<Int>?,
    private var list_stok:ArrayList<Int>?,
    private var list_total_unit:ArrayList<Int>?,
    private  var list_total_penjualan:ArrayList<Int>?,
    private  var list_tanggal:ArrayList<Int>?
        ) :RecyclerView.Adapter<Adapter_Laporan.MyHolder>() {
            override fun dummy() {}


}