package com.example.pos.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.database
import com.example.pos.Model.model_barang
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_pembayaran

class Pembayaran : AppCompatActivity() {
    lateinit var btn_cetak:ImageView
    lateinit var rc:RecyclerView
    lateinit var adapter:Adapter_pembayaran
    lateinit var total_beli_pem:TextView
    var total_uang:Int = 0
    lateinit var rv:RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        //assign variable
        total_beli_pem = findViewById(R.id.total_beli_pembayaran)
        btn_cetak = findViewById(R.id.to_cetak)
        rc = findViewById(R.id.rc_pembayaran)
        rv = findViewById(R.id.rv_empty)
        //recycleView
        rc.setHasFixedSize(true)
        rc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


    }
    fun RetrieveData():ArrayList<model_barang>{
        lateinit var listItem:ArrayList<model_barang>
        var list_kode:ArrayList<String> = intent.getSerializableExtra("key_kode") as ArrayList<String>
        var kode_lt = LinkedHashSet(list_kode).toMutableSet()
        var jumlah = intent.getStringExtra("key_uang")
        val list_harga :HashMap<String, Int>?= intent.getSerializableExtra("key_harga") as HashMap<String, Int>?
        var list_nama:ArrayList<String> = intent.getSerializableExtra("key_nama") as ArrayList<String>
        var nama_lt = LinkedHashSet(list_nama).toMutableSet()
        var list_jenis= intent.getSerializableExtra("key_jenis") as HashMap<String, String>?
        var list_jml = intent.getSerializableExtra("key_jumlah") as HashMap<String, Int>?
        var hargaList:ArrayList<Int> = ArrayList()
        var jenisList:ArrayList<String> = ArrayList()
        var jumlahList:ArrayList<Int> = ArrayList()
        var list_kode2:ArrayList<String> = ArrayList()
        var list_nama2:ArrayList<String> = ArrayList()
        for (i in nama_lt){
            list_harga?.get(i)?.let { hargaList.add(it) }
            list_jenis?.get(i)?.let { jenisList.add(it) }
            list_jml?.get(i)?.let { jumlahList.add(it) }
        }
        for(i in kode_lt){
            list_kode2.add(i)
        }
        for(i in nama_lt){
            list_nama2.add(i)
        }

        total_beli_pem.text = "Rp." + jumlah

        listItem = ArrayList()
        if(list_kode2.isEmpty()){
            total_beli_pem.text = "Rp.0"
            rv.visibility = View.VISIBLE
        }else{
            adapter = Adapter_pembayaran(this@Pembayaran,list_kode2, list_nama2,hargaList, jenisList, jumlahList)
            rc.adapter = adapter
        }

        //go to halaman cetak struk
        var log2:Intent = Intent(this@Pembayaran, Struk_Activity::class.java)
        log2.putExtra("key_kode", list_kode2)
        log2.putExtra("key_nama", list_nama2)
        log2.putExtra("key_harga", hargaList)
        log2.putExtra("key_jenis", jenisList)
        log2.putExtra("key_jumlah", jumlahList)
        btn_cetak.setOnClickListener{
            startActivity(log2)
        }

        return listItem
    }

    override fun onResume() {
        super.onResume()
        RetrieveData()
        try{

        }catch (e:Exception){
            Toast.makeText(this, "Error reading\n" + e, Toast.LENGTH_SHORT).show()
        }
    }
}