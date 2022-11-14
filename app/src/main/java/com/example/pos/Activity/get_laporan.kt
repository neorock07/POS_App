package com.example.pos.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import com.example.pos.RecycleView.Adapter_Laporan

class get_laporan : AppCompatActivity() {
    private lateinit var list_id:ArrayList<Int>
    private lateinit var list_kode:ArrayList<String>
    private lateinit var list_nama:ArrayList<String>
    private lateinit var list_jenis:ArrayList<String>
    private lateinit var list_harga:ArrayList<Int>
    private lateinit var list_stok:ArrayList<Int>
    private lateinit var list_total_unit:ArrayList<Int>
    private lateinit var list_total_penjualan:ArrayList<Int>
    private lateinit var list_tanggal:ArrayList<Int>
    private lateinit var mainActivity: MainActivity
    private  lateinit var  adapter: Adapter_Laporan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_get_laporan)
    }
}