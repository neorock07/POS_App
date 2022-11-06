package com.example.pos.Activity

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.database
import com.example.pos.Model.model_barang
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_pembayaran
import com.example.pos.RecycleView.CustomAdapter
import java.util.LinkedHashSet

class Pembayaran : AppCompatActivity() {
    lateinit var btn_cetak:ImageView
    lateinit var rc:RecyclerView
    lateinit var adapter:Adapter_pembayaran
    lateinit var total_beli_pem:TextView
    var total_uang:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        //assign variable
        total_beli_pem = findViewById(R.id.total_beli_pembayaran)
        btn_cetak = findViewById(R.id.to_cetak)
        rc = findViewById(R.id.rc_pembayaran)
        //recycleView
        rc.setHasFixedSize(true)
        rc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //go to halaman cetak struk
        btn_cetak.setOnClickListener{
            startActivity(Intent(this@Pembayaran, Struk_Activity::class.java))
        }
    }
    fun RetrieveData():ArrayList<model_barang>{
        lateinit var listItem:ArrayList<model_barang>
        var list_kode:ArrayList<String> = intent.getSerializableExtra("key_kode") as ArrayList<String>
        var kode_lt = LinkedHashSet(list_kode).toMutableSet()
        var jumlah = intent.getStringExtra("key_uang")
        var list_harga:ArrayList<Int> = intent.getSerializableExtra("key_harga") as ArrayList<Int>
        var list_nama:ArrayList<String> = intent.getSerializableExtra("key_nama") as ArrayList<String>
        var nama_lt = LinkedHashSet(list_nama).toMutableSet()
        var list_jenis:ArrayList<String> = intent.getSerializableExtra("key_jenis") as ArrayList<String>
        var list_jml:ArrayList<Int> = intent.getSerializableExtra("key_jumlah") as ArrayList<Int>
        var list_kode2:ArrayList<String> = ArrayList()
        var list_nama2:ArrayList<String> = ArrayList()
        for(i in kode_lt){
            list_kode2.add(i)
        }
        for(i in nama_lt){
            list_nama2.add(i)
        }
        total_beli_pem.text = "Rp." + jumlah
        val db: database = database(this)
        //var cursor: Cursor = db.RetDatafromKode(list_kode[0])
        listItem = ArrayList()
        Toast.makeText(this, "List : " + list_kode2.toString() + list_harga.toString() + list_nama2.toString(), Toast.LENGTH_SHORT).show()
//        val alert:AlertDialog = AlertDialog.Builder(this)
//            .setMessage(list_kode2.toString() + list_harga.toString() + list_nama2.toString() + list_jml.toString())
//            .show()
        adapter = Adapter_pembayaran(this@Pembayaran,list_kode2, list_nama2,list_harga, list_jenis, list_jml)
        //listItem.add(model_barang(list_kode.toString(),list_nama.toString(),list_harga,list_jenis,list_jml,jumlah!!))
//            while(cursor!!.moveToNext()){
//                //var list_kode = cursor.getString(0)
//                var list_nama = cursor.getString(1)
//                var list_harga = cursor.getInt(2)
//                var list_jenis = cursor.getString(3)
//                var list_stok = cursor.getInt(4)
//                Toast.makeText(this,"Nama : " + list_nama, Toast.LENGTH_SHORT).show()
//
//            }
//        adapter = Adapter_pembayaran(this, listItem,12000)
        rc.adapter = adapter
        return listItem

//        }
//
//    }
}
//    fun getAdapter2() : Adapter_pembayaran {
//        adapter = Adapter_pembayaran(this,RetrieveData())
//        rc.adapter = adapter
//        return adapter
//    }

    override fun onResume() {
        super.onResume()
        RetrieveData()
        try{

        }catch (e:Exception){
            Toast.makeText(this, "Error reading\n" + e, Toast.LENGTH_SHORT).show()
        }
    }
}