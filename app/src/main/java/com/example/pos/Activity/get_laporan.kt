package com.example.pos.Activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
import com.example.pos.Model.model_laporan
import android.widget.Adapter
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_Laporan


class get_laporan : AppCompatActivity() {
    private lateinit var list_id: ArrayList<Int>
    private lateinit var list_kode: ArrayList<String>
    private lateinit var list_nama: ArrayList<String>
    private lateinit var list_jenis: ArrayList<String>
    private lateinit var list_harga: ArrayList<Int>
    private lateinit var list_stok: ArrayList<Int>
    private lateinit var list_total_unit: ArrayList<Int>
    private lateinit var list_total_penjualan: ArrayList<Int>
    private lateinit var list_tanggal: ArrayList<Int>
    private lateinit var recyclerView: RecyclerView
    private lateinit var mainActivity: MainActivity
    private lateinit var adapter: Adapter_Laporan
    lateinit var search: androidx.appcompat.widget.SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_laporan)
        search = findViewById(R.id.search_all)
        mainActivity = MainActivity()

        tahun = findViewById(R.id.tahun_laporan)
        bulan = findViewById(R.id.bulan_laporan)
        hari = findViewById(R.id.hari_laporan)
        recyclerView = findViewById(R.id.rc_listLaporan)
        recyclerView.setHasFixedSize(true)
        adapter = getAdapterLaporan()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        search.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filter(newText)
                return false
            }
        })
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
//        setContentView(R.layout.activity_get_laporan)
    }

    fun readLaporanAll(): ArrayList<model_laporan> {
        val db: Database = Database(this)
        val cursor: Cursor = db.viewLaporan()
        val modelItemx = ArrayList<model_laporan>
        if (cursor.count > 0) {
            while (cursor!!.moveToNext()) {
                var list_id: cursor.getInt(0)
                var list_kode: cursor.getString(1)
                var list_nama: cursor.getString(2)
                var list_jenis: cursor.getString(3)
                var list_harga: cursor.getInt(4)
                var list_stok: cursor.getInt(5)
                var list_total_unit: cursor.getInt(6)
                var list_total_penjualan: cursor.getInt(7)
                var list_tanggal: cursor.getInt(8)
                modelItemx.add(
                    model_laporan(
                        list_id,
                        list_kode,
                        list_nama,
                        list_jenis,
                        list_harga,
                        list_stok,
                        list_total_unit,
                        list_total_penjualan,
                        list_tanggal
                    )
                )
            }
        }
        return modelItemx
    }

    fun getAdapterLaporan(): Adapter_Laporan {
        adapter = Adapter_Laporan(this, readLaporanAll())
        adapter!!.setWhenClickListener(object : Adapter_Laporan.OnItemsClickListener {
            override fun onItemClick(refresh: Boolean) {
                if (refresh) {
//                    val i = Intent(this@daftar_laporan, daf)
                }
            }
        })
    }
}