package com.example.pos.Activity

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
import com.example.pos.Model.model_harian
import com.example.pos.Model.model_harian_detail
import com.example.pos.Model.model_tahunan
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_Dft_Harian_Detail
import com.example.pos.RecycleView.Adapter_Lpr_Bulanan
import com.example.pos.RecycleView.Adapter_Lpr_Harian
import com.example.pos.RecycleView.Adapter_Lpr_Tahunan

class Laporan_Harian : AppCompatActivity() {

    private lateinit var rc: RecyclerView
    private lateinit var adapter: Adapter_Lpr_Harian
    private lateinit var list_data: ArrayList<model_harian>
    private lateinit var list_data2: ArrayList<model_harian_detail>
    private lateinit var db: Database
    private lateinit var txt_bulan: TextView
    private lateinit var name_tahun: String
    private lateinit var name_bulan: String
    private lateinit var name_bulan1: String
    private lateinit var kembali: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_harian)
        rc = findViewById(R.id.Recycleview_laporan)
        txt_bulan = findViewById(R.id.txt_bulan)
        rc.setHasFixedSize(true)
        rc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        name_tahun = intent.getStringExtra("Key_Tahun")!!
        name_bulan = intent.getStringExtra("Key_Bulan")!!
        txt_bulan.text = name_bulan
        kembali = findViewById(R.id.kembali)


        if (name_bulan == "Januari"){
            name_bulan1 = '1'.toString()
        }else if (name_bulan == "Februari"){
            name_bulan1 = '2'.toString()
        }else if(name_bulan == "Maret"){
            name_bulan1 = '3'.toString()
        }else if(name_bulan == "April"){
            name_bulan1 = '4'.toString()
        }else if(name_bulan == "Mei"){
            name_bulan1 = '5'.toString()
        }else if(name_bulan == "Juni"){
            name_bulan1 = '6'.toString()
        }else if(name_bulan == "Juli"){
            name_bulan1 = '7'.toString()
        }else if(name_bulan == "Agustus"){
            name_bulan1 = '8'.toString()
        }else if(name_bulan == "September"){
            name_bulan1 = '9'.toString()
        }else if(name_bulan == "Oktober"){
            name_bulan1 = "10"
        }else if(name_bulan == "November"){
            name_bulan1 = "11"
        }
        else{
            name_bulan1 = "12"
        }
        readDataDay(name_bulan1, name_tahun)
        kembali.setOnClickListener(){
            onBackPressed()
        }
    }
    private fun readDataDay(bulan: String, tahun: String){
        var tanggal = ""
        list_data = ArrayList()
        db = Database(this)
        val cursor: Cursor = db.readPerTanggal(bulan, tahun)
        if(cursor.count > 0){
            while(cursor.moveToNext()){
                 tanggal = cursor.getString(0)
                list_data.add(
                    model_harian(
                        tanggal,
                        name_bulan,
                        name_tahun,
                        readDataDay1(tanggal,name_tahun,name_bulan1)
                    )
                )
            }
        }
        Toast.makeText(this, tanggal.toString(), Toast.LENGTH_LONG).show()
        adapter = Adapter_Lpr_Harian(this,list_data)
        rc.adapter = adapter
        adapter.notifyDataSetChanged()
    }
    private fun readDataDay1(tanggal: String, tahun: String, bulan: String): ArrayList<model_harian_detail>{
        list_data2 = ArrayList()
        var list_nama = ArrayList<String>()
        var list_kode = ArrayList<String>()
        var list_harga = ArrayList<String>()
        var list_jenis = ArrayList<String>()
        var list_jumlah = ArrayList<Int>()
        db = Database(this)
        val cursor: Cursor = db.readPerDetail(tanggal, tahun, bulan)
        if(cursor.count > 0) {
            while (cursor.moveToNext()) {
                 list_kode.add(cursor.getString(0))
                 list_nama.add(cursor.getString(1))
                list_harga.add(cursor.getString(2))
                list_jenis.add(cursor.getString(3))
                list_jumlah.add(cursor.getInt(4))
            }
            for(item in list_kode.indices){
                var jumlah = 0
                var cek : Int = 0
                for(item1 in list_kode.indices){
                    if(item>item1 && list_kode[item]==list_kode[item1]){
                        cek=2
                        break
                    }
                    else if(list_kode[item]==list_kode[item1]){
                        jumlah += list_jumlah[item1]
                    }
                }
                if(cek==2){
                    continue
                }
                var nama = list_nama[item]
                var harga = list_harga[item]
                var jenis = list_jenis[item]
                list_data2.add(
                    model_harian_detail(
                        nama, harga, jenis, jumlah
                    )
                )
            }
        }
        return list_data2
    }
    override fun onResume() {
        super.onResume()
        readDataDay(name_bulan1,name_tahun)
    }
}