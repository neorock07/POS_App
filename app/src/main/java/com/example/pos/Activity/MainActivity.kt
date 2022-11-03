package com.example.pos.Activity

import com.example.pos.Database.database
import com.example.pos.RecycleView.CustomAdapter
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Model.model_barang
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
    lateinit var search:androidx.appcompat.widget.SearchView
    lateinit var recyclerview: RecyclerView
    lateinit var frameRefresh: FrameLayout
    lateinit var totalBeli: TextView
    var total: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview = findViewById<RecyclerView>(R.id.listBarang)
        totalBeli = findViewById(R.id.total_beli)
        settingsFAB = findViewById(R.id.setting)
        recyclerview.setHasFixedSize(true)
        frameRefresh = findViewById(R.id.refresh)
        recyclerview.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        search = findViewById(R.id.search_all)

        settingsFAB.setOnClickListener{
            intent = Intent(this, daftar_barang::class.java)
            startActivity(intent).also{
                total = 0
                totalBeli.text = "Rp0"
            }
        }
        //search view query
        search.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange( newText: String?): Boolean {
                adapter.filter.filter(newText)
                adapter.notifyDataSetChanged()
                recyclerview.adapter!!.notifyDataSetChanged()
                return false
            }

        })
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
            adapter!!.setWhenClickListener(object : CustomAdapter.OnItemsClickListener{
                override fun onItemClick(harga: Int) {
                    total +=harga
                    totalBeli.text  = NumberFormat(total.toString())
                }

            })
            recyclerview.adapter = adapter
        }
    }

    override fun onResume() {
        super.onResume()
        readAll()
    }
    fun NumberFormat(s:String):String{
        var current:String = ""
        var parsed:Double
        var cleanString:String = s.replace("""[,.]""".toRegex(), "")
        parsed = cleanString.toDouble()
        var formatted:String = java.text.NumberFormat.getNumberInstance().format(parsed)
        current = formatted
        return current
    }

}