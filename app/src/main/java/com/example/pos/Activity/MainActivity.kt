package com.example.pos.Activity

import com.example.pos.Database.database
import com.example.pos.RecycleView.CustomAdapter
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.pos.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var settingsFAB: CardView
    lateinit var list_nama: ArrayList<String>
    lateinit var list_harga: ArrayList<Int>
    lateinit var list_jenis: ArrayList<String>
    lateinit var list_kode: ArrayList<String>
    lateinit var list_stok: ArrayList<Int>
    lateinit var adapter: CustomAdapter
    lateinit var recyclerview: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview = findViewById<RecyclerView>(R.id.listBarang)
        settingsFAB = findViewById(R.id.setting)
        recyclerview.setHasFixedSize(true)
        // Setting the Adapter with the recyclerview

        recyclerview.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)

        settingsFAB.setOnClickListener{
            intent = Intent(this, formBarang::class.java)
            startActivity(intent)
        }

    }
    fun readAll(){
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
            adapter = CustomAdapter(this,list_kode,list_nama,list_harga,list_jenis,list_stok)
            recyclerview.adapter = adapter
        }

    }

    override fun onResume() {
        super.onResume()
        readAll()
    }
}