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
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Model.model_barang
import com.example.pos.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    lateinit var settingsFAB: CardView
    lateinit var adapter: CustomAdapter
    lateinit var search:androidx.appcompat.widget.SearchView
    lateinit var recyclerview: RecyclerView
    lateinit var frameRefresh: FrameLayout
    lateinit var totalBeli: TextView
    lateinit var listitem: ArrayList<model_barang>
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
        listitem = readAll()
        adapter = getAdapter2()
        recyclerview.adapter=adapter

        settingsFAB.setOnClickListener{
            intent = Intent(this, daftar_barang::class.java)
            startActivity(intent).also{
                total = 0
                totalBeli.text = "Rp0"
            }
        }
        //search view query
        search.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
                }
        })
       adapter.notifyDataSetChanged()
    }


    fun readAll() : ArrayList<model_barang>{

        val db: database = database(this)
        var cursor: Cursor = db.viewBarang()
        val modelItemx = ArrayList<model_barang>()
        if (cursor.count>0){
            while (cursor!!.moveToNext()){
                var list_kode = cursor.getString(0)
                var list_nama = cursor.getString(1)
                var list_harga = cursor.getInt(2)
                var list_jenis = cursor.getString(3)
                var list_stok = cursor.getInt(4)
                modelItemx.add(model_barang(list_kode,list_nama,list_harga,list_jenis,list_stok))

            }

    }
        return modelItemx
    }

    fun getAdapter2() : CustomAdapter{
        adapter = CustomAdapter(this,readAll())
        adapter!!.setWhenClickListener(object : CustomAdapter.OnItemsClickListener{
            override fun onItemClick(harga: Int) {
                total +=harga
                totalBeli.text  = NumberFormat(total.toString())
            }

        })
        recyclerview.adapter = adapter
        return adapter
    }


    override fun onResume() {
        super.onResume()
        adapter=getAdapter2()
        adapter.notifyDataSetChanged()
        recyclerview.adapter = adapter
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
    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<model_barang> = ArrayList()

        // running a for loop to compare elements.
        for (item in listitem) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.nama.toLowerCase().contains(text.toLowerCase())||item.kode.contains(text)) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist)
        }
    }
}