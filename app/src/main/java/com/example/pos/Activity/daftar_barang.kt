package com.example.pos.Activity

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.database
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_Dft_Barang
import com.example.pos.RecycleView.CustomAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class daftar_barang : AppCompatActivity() {
    private lateinit var btn_add_barang:FloatingActionButton
    private lateinit var back:ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var list_kode:ArrayList<String>
    private lateinit var list_nama:ArrayList<String>
    private lateinit var list_jenis:ArrayList<String>
    private lateinit var list_stok:ArrayList<Int>
    private lateinit var list_harga:ArrayList<Int>
    private lateinit var adapter:Adapter_Dft_Barang
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_barang)

        //assign variable
        btn_add_barang = findViewById(R.id.add_barang)
        back = findViewById(R.id.back_barang)
        recyclerView = findViewById(R.id.rc_listBarang)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        btn_add_barang.setOnClickListener{
            startActivity(Intent(this@daftar_barang, formBarang::class.java))
            finish()
        }
        back.setOnClickListener{
            startActivity(Intent(this@daftar_barang, MainActivity::class.java))
            finish()
        }

    }
    fun readBarangAll(){
        val db: database = database(this)
        var cursor: Cursor = db.viewBarang()
        list_jenis = ArrayList()
        list_harga = ArrayList()
        list_kode = ArrayList()
        list_nama = ArrayList()
        list_stok = ArrayList()

        if (cursor.count>0){
            while (cursor!!.moveToNext()){
                list_kode.add(cursor.getString(0))
                list_nama.add(cursor.getString(1))
                list_harga.add(cursor.getInt(2))
                list_jenis.add(cursor.getString(3))
                list_stok.add(cursor.getInt(4))
            }
            adapter = Adapter_Dft_Barang(this,list_kode,list_jenis,list_nama,list_harga,list_stok)
            recyclerView.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        readBarangAll()
    }

}