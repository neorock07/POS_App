package com.example.pos.Activity

import android.content.Context
import com.example.pos.Database.Database
import com.example.pos.RecycleView.CustomAdapter
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Model.model_barang
import com.example.pos.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

import java.lang.reflect.Type
import java.util.LinkedHashMap


class MainActivity : AppCompatActivity() {
    lateinit var settingsFAB: CardView
    lateinit var adapter: CustomAdapter
    lateinit var search: androidx.appcompat.widget.SearchView
    lateinit var recyclerview: RecyclerView
    lateinit var frameRefresh: FrameLayout
    lateinit var totalBeli: TextView
    lateinit var listitem: ArrayList<model_barang>
    var nama_arr:ArrayList<String> = ArrayList()
    var total: Int = 0
    lateinit var modelItemx:ArrayList<model_barang>
    var arr_kode: ArrayList<String> = ArrayList()
    var arr_harga = HashMap<String, Int>()
    private var arr_nama: ArrayList<String> = ArrayList()
    var arr_jenis = HashMap<String, String>()
    var arr_jumlah = HashMap<String, Int>()
    var bundle:Bundle = Bundle()
    lateinit var btn_bayar: ImageView
    private lateinit var sharedPref_total: SharedPreferences.Editor
    private lateinit var sharedJumlah: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var log:Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        var kembali: ImageView= findViewById(R.id.kembali)
        recyclerview = findViewById<RecyclerView>(R.id.listBarang)
        totalBeli = findViewById(R.id.total_beli)
        settingsFAB = findViewById(R.id.setting)
        recyclerview.setHasFixedSize(true)
        frameRefresh = findViewById(R.id.refresh)
        btn_bayar = findViewById(R.id.keranjang)
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        search = findViewById(R.id.search_all)
        listitem = readAll()
        adapter = getAdapter2()
        recyclerview.adapter = adapter

        //sharedPreferences
        sharedPref_total = getSharedPreferences("key", Context.MODE_PRIVATE).edit()
        sharedJumlah = applicationContext.getSharedPreferences("key_item", 0)
        editor = sharedJumlah.edit()

        settingsFAB.setOnClickListener {
            intent = Intent(this, form_login::class.java)
            startActivity(intent).also {
                total = 0
                totalBeli.text = "0"
            }
        }
        kembali.setOnClickListener {
            finishAffinity()
        }
        //search view query
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

        adapter.notifyDataSetChanged()

        if(savedInstanceState != null){
            val bundle1:Bundle = intent.getBundleExtra("Data_Item")!!
          //  var list: Bundle? = savedInstanceState.getBundle("LIST_JUMLAH")
            var hrg:Int? = savedInstanceState.getInt("Key_Harga_Save")
            if(bundle1 != null ){
                bundle = bundle1
                total = hrg!!

            }

        }

        btn_bayar.setOnClickListener {
            startActivity(log)
        }

    }

    fun readAll(): ArrayList<model_barang> {
        val db: Database = Database(this)
        var cursor: Cursor = db.viewBarang()
         modelItemx = ArrayList<model_barang>()
        if (cursor.count > 0) {
            while (cursor!!.moveToNext()) {
                var list_kode = cursor.getString(0)
                var list_nama = cursor.getString(1)
                var list_harga = cursor.getInt(2)
                var list_jenis = cursor.getString(3)
                var list_stok = cursor.getInt(4)
                modelItemx!!.add(
                    model_barang(
                        list_kode,
                        list_nama,
                        list_harga,
                        list_jenis,
                        list_stok,
                        totalBeli.text.toString()
                    )
                )
                nama_arr.add(list_nama)
            }
        }
        return modelItemx
    }

    fun getAdapter2(): CustomAdapter {
        adapter = CustomAdapter(this, readAll(),bundle)
        adapter!!.setWhenClickListener(object : CustomAdapter.OnItemsClickListener {

            override fun onItemClick(harga: Int) {
                total += harga
                totalBeli.text = NumberFormat(total.toString())
            }
            override fun onArrayItemClick(
                kode: ArrayList<String>,
                Arr_harga: HashMap<String, Int>,
                nama: ArrayList<String>,
                jenis: HashMap<String, String>,
                jumlah_total: HashMap<String, Int>
            ) {
                arr_kode = kode
                arr_nama = nama

                for (i in arr_nama) {
                    arr_harga[i] = Arr_harga[i]!!
                    arr_jumlah[i] = jumlah_total[i]!!
                    arr_jenis[i] = jenis[i]!!
                    bundle.putInt(i, arr_jumlah.get(i)!!)
                }
                 log = Intent(this@MainActivity, Pembayaran::class.java)

                log.putExtra("key_kode", arr_kode)
                log.putExtra("key_nama", arr_nama)
                log.putExtra("key_jenis", arr_jenis)
                //log.putExtra("key_jumlah", arr_jumlah)
                log.putExtra("key_harga", arr_harga)

                if(arr_jumlah.isEmpty() && !bundle.isEmpty){
                    log.putExtra("key_jumlah", bundle)
                }else{
                    log.putExtra("key_jumlah", arr_jumlah)
                }

                if (arr_kode.isEmpty()) {
                    log.putExtra("key_uang", "Rp.0")
                } else {
                    log.putExtra("key_uang", total)
                }

            }

        })
        recyclerview.adapter = adapter
        return adapter
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putBundle("LIST_JUMLAH",bundle)
        outState.putInt("Key_Harga_Save", total)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onResume() {
        super.onResume()
        adapter = getAdapter2()
        adapter.notifyDataSetChanged()
        recyclerview.adapter = adapter
        //adapter to load all data jumlah
        Toast.makeText(this, bundle.toString(),Toast.LENGTH_LONG).show()
    }

    fun NumberFormat(s: String): String {
        var current: String = ""
        var parsed: Double
        var cleanString: String = s.replace("""[,.]""".toRegex(), "")
        parsed = cleanString.toDouble()
        var formatted: String = java.text.NumberFormat.getNumberInstance().format(parsed)
        current = formatted
        return current
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<model_barang> = ArrayList()
        val kembali: ImageView= findViewById(R.id.kembali)

        // running a for loop to compare elements.
        for (item in listitem) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.nama.lowercase()
                    .contains(text.lowercase()) || item.kode.contains(text) || item.jenis.contains(
                    text.lowercase()
                )
            ) {
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
        kembali.setOnClickListener{
            onBackPressed()
        }
    }
}