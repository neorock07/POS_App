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
import com.example.pos.Model.model_barang
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_Dft_Barang
import com.google.android.material.floatingactionbutton.FloatingActionButton

class daftar_barang : AppCompatActivity() {
    private lateinit var btn_add_barang: FloatingActionButton
    private lateinit var back: ImageView
    private lateinit var recyclerView: RecyclerView
    private lateinit var list_kode: ArrayList<String>
    private lateinit var list_nama: ArrayList<String>
    private lateinit var list_jenis: ArrayList<String>
    private lateinit var list_stok: ArrayList<Int>
    private lateinit var list_harga: ArrayList<Int>
    private lateinit var mainActivity: MainActivity
    private lateinit var adapter: Adapter_Dft_Barang
    lateinit var search: androidx.appcompat.widget.SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_barang)
        search = findViewById(R.id.search_all)
        mainActivity = MainActivity()
        //assign variable
        btn_add_barang = findViewById(R.id.add_barang)
        back = findViewById(R.id.kembali)
        recyclerView = findViewById(R.id.rc_listBarang)
        recyclerView.setHasFixedSize(true)
        adapter = getAdapter2()
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        btn_add_barang.setOnClickListener {
            startActivity(Intent(this@daftar_barang, formBarang::class.java))
            finish()
        }
        back.setOnClickListener {
            startActivity(Intent(this@daftar_barang, MainActivity::class.java))
            finish()
        }
        search.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
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
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    fun readBarangAll(): ArrayList<model_barang> {
        val db: Database = Database(this)
        var cursor: Cursor = db.viewBarang()
        val modelItemx = ArrayList<model_barang>()
        if (cursor.count > 0) {
            while (cursor!!.moveToNext()) {
                var list_kode = cursor.getString(0)
                var list_nama = cursor.getString(1)
                var list_harga = cursor.getInt(2)
                var list_jenis = cursor.getString(3)
                var list_stok = cursor.getInt(4)
                modelItemx.add(
                    model_barang(
                        list_kode,
                        list_nama,
                        list_harga,
                        list_jenis,
                        list_stok,
                        "0"
                    )
                )

            }

        }
        return modelItemx
    }

    fun getAdapter2(): Adapter_Dft_Barang {
        adapter = Adapter_Dft_Barang(this, readBarangAll())
        adapter!!.setWhenClickListener(object : Adapter_Dft_Barang.OnItemsClickListener {
            override fun onItemClick(refresh: Boolean) {
                if (refresh) {
                    val i = Intent(this@daftar_barang, daftar_barang::class.java)
                    finish()
                    overridePendingTransition(0, 0)
                    startActivity(i)
                    overridePendingTransition(0, 0)
                }
            }

        })
        recyclerView.adapter = adapter
        return adapter
    }

    override fun onResume() {
        super.onResume()
        readBarangAll()
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<model_barang> = ArrayList()

        // running a for loop to compare elements.
        for (item in readBarangAll()) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.nama.toLowerCase().contains(text.toLowerCase()) || item.kode.contains(text)) {
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