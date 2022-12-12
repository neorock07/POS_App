package com.example.pos.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
import com.example.pos.Model.model_barang
import com.example.pos.R
import com.example.pos.RecycleView.CustomAdapter
import com.example.pos.RecycleView.CustomAdapter.OnItemsClickListener
import com.example.pos.Toast.showCustomToast
import java.lang.NullPointerException
import java.nio.BufferUnderflowException

import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
    lateinit var settingsFAB: CardView
    lateinit var adapter: CustomAdapter
    lateinit var search: SearchView
    lateinit var recyclerview: RecyclerView
    lateinit var frameRefresh: FrameLayout
    lateinit var totalBeli: TextView
    lateinit var listitem: ArrayList<model_barang>
    var nama_arr:ArrayList<String> = ArrayList()
    var total: Int = 0
    lateinit var modelItemx:ArrayList<model_barang>
    lateinit var modelItemz:ArrayList<model_barang>
    var arr_kode: ArrayList<String> = ArrayList()
    var arr_harga : ArrayList<Int> = ArrayList()
    private var arr_nama: ArrayList<String> = ArrayList()
    var arr_jenis : ArrayList<String> = ArrayList()
    var arr_jumlah : ArrayList<Int> = ArrayList()
    var arr_stok2 : ArrayList<Int> = ArrayList()
    var bundle:Bundle = Bundle()
    var bundle1:Bundle = Bundle()
    var bundle2:Bundle = Bundle()

    lateinit var btn_bayar: ImageView
    private lateinit var sharedPref_total: SharedPreferences.Editor
    private lateinit var sharedJumlah: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var log:Intent
    private lateinit var list_kode:String
    private lateinit var list_nama:String
    var list_harga:Int = 0
    var list_stok:Int = 0
    var cek:Boolean = true
    private lateinit var list_jenis:String
    private  var bundle1_2:Bundle?  = null
    private  var bundle2_2:Bundle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        var kembali:ImageView   = findViewById(R.id.kembali)
        recyclerview            = findViewById<RecyclerView>(R.id.listBarang)
        totalBeli               = findViewById(R.id.total_beli)
        settingsFAB             = findViewById(R.id.setting)
        recyclerview.setHasFixedSize(true)
        frameRefresh             = findViewById(R.id.refresh)
        btn_bayar                = findViewById(R.id.keranjang)
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        search                   = findViewById(R.id.search_all)
        listitem                 = readAll()
        adapter                  = getAdapter2() //adapterBarang
        recyclerview.adapter     = adapter

        //sharedPreferences
        sharedPref_total = getSharedPreferences("key", MODE_PRIVATE).edit()
        sharedJumlah = applicationContext.getSharedPreferences("key_item", 0)
        editor = sharedJumlah.edit()
        //GET TOTAL HARGA
        var total2 : Int = intent.getIntExtra("Data_Total_Harga", 0)
        total = total2
        totalBeli.text = NumberFormat(total.toString())
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
            SearchView.OnQueryTextListener {
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
        kembali.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        btn_bayar.setOnClickListener{
            if (total!=0){
                if (arr_jumlah.isEmpty()){
                    modelItemz = ArrayList<model_barang>()
                    modelItemz = readAll()
                  for (i in modelItemz.indices){
                      arr_kode.add(modelItemz.get(i).kode)
                      arr_harga.add(modelItemz.get(i).harga)
                      arr_nama.add(modelItemz.get(i).nama)
                      arr_jenis.add(modelItemz.get(i).jenis)
                      arr_stok2.add(modelItemz.get(i).stok)
                      arr_jumlah.add(0)
                  }
                    for (i in arr_nama.indices){
                        for (p in 0 until RetKode().size){
                            if (arr_nama[i] == RetKode().keys.elementAt(p)){
                                arr_jumlah[i] = RetBundle().get(RetBundle().keys.elementAt(p))!!
                                arr_stok2[i] = RetStok().get(RetStok().keys.elementAt(p))!!
                            }
                        }
                    }

                }

                log = Intent(this@MainActivity, Pembayaran::class.java)
                log.putExtra("key_kode", arr_kode)
                log.putExtra("key_nama", arr_nama)
                log.putExtra("key_jenis", arr_jenis)
                //log.putExtra("key_jumlah", arr_jumlah)
                log.putExtra("key_harga", arr_harga)
                log.putExtra("key_stok", arr_stok2)
                log.putExtra("key_jumlah", arr_jumlah)
                if (arr_kode.isEmpty()) {
                    log.putExtra("key_uang", "Rp.0")
                } else {
                    log.putExtra("key_uang", total)
                }
                startActivity(log)
            }else{
                Toast(this).showCustomToast (this)
                Toast.makeText(this, "Data kode : $arr_kode\nData nama : $arr_nama\nRetNama : ${RetNama()}\nRetKode : ${RetKode()}",Toast.LENGTH_LONG).show()
            }

        }
    }

//function untuk get data jumlah from Pembayaran
    fun RetBundle():HashMap<String, Int>{
        var data:HashMap<String, Int> = HashMap()
        try{
            data = intent.getSerializableExtra("Data_Jumlah")!! as HashMap<String, Int>
        }catch (e:NullPointerException){

        }
        Toast.makeText(this, "Hash : ${data.toString()}",Toast.LENGTH_LONG).show()
        return data
    }

    //function untuk get data stok from Pembayaran
    fun RetStok():HashMap<String, Int>{
        var hash:HashMap<String,Int> = HashMap()
        try{
            hash = intent.getSerializableExtra("Data_Stok")!! as HashMap<String, Int>
        }catch (e:NullPointerException){

        }
        Toast.makeText(this, "Stok : ${hash.toString()}",Toast.LENGTH_LONG).show()
        return hash
    }
    //function untuk get data nama from Pembayara
    fun RetNama():HashMap<String, String> {
        var hash: HashMap<String, String> = HashMap()
        try {
            hash = intent.getSerializableExtra("Data_Nama")!! as HashMap<String, String>
        } catch (e: NullPointerException) {

        }
        Toast.makeText(this, "Nama : ${hash.toString()}", Toast.LENGTH_LONG).show()
        return hash
    }
    //function untuk get data Kode from Pembayara
    fun RetKode():HashMap<String, String> {
        var hash: HashMap<String, String> = HashMap()
        try {
            hash = intent.getSerializableExtra("Data_Kode")!! as HashMap<String, String>
        } catch (e: NullPointerException) {

        }
        Toast.makeText(this, "Kode : ${hash.toString()}", Toast.LENGTH_LONG).show()
        return hash
    }


        fun readAll(): ArrayList<model_barang> {
        val db: Database = Database(this)
        var cursor: Cursor = db.viewBarang()
        modelItemx = ArrayList<model_barang>()
        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                list_kode = cursor.getString(0)
                list_nama = cursor.getString(1)
                list_harga = cursor.getInt(2)
                list_jenis = cursor.getString(3)
                list_stok = cursor.getInt(4)
                var inputJumlah : Int = 0

                modelItemx.add(
                    model_barang(
                        list_kode,
                        list_nama,
                        list_harga,
                        list_jenis,
                        list_stok,
                        inputJumlah
                    )
                )
            }

            }

        return modelItemx
    }

    private fun getAdapter2(): CustomAdapter {
        //hm sebagai default value jika belum pernah intent dari pembayaran
        val hm:HashMap<String, Int> = HashMap()
        if(RetBundle() != null){
            adapter = CustomAdapter(this, readAll(), RetBundle(), RetStok())
        }else{
            adapter = CustomAdapter(this, readAll(),hm,hm)
        }
        adapter.setWhenClickListener(object : OnItemsClickListener {

            override fun onItemClick(harga: Int) {
                total += harga
                totalBeli.text = NumberFormat(total.toString())
            }
            override fun onArrayItemClick(
                kode: ArrayList<String>,
                Arr_harga: ArrayList<Int>,
                nama: ArrayList<String>,
                jenis: ArrayList<String>,
                arr_jmlh: ArrayList<Int>,
                arr_stok: ArrayList<Int>
            ) {

                val nama_sementara:ArrayList<String> = ArrayList()
                val kode_sementara:ArrayList<String> = ArrayList()

                if(kode.isNotEmpty() || nama.isNotEmpty()){
                   arr_kode = kode
                    arr_jumlah = arr_jmlh
                    arr_stok2 = arr_stok
                    arr_nama = nama
                    arr_jenis = jenis
                    arr_harga = Arr_harga
                }else{
                    if(!RetNama().isEmpty() || !RetKode().isEmpty() && kode.isEmpty() && nama.isEmpty()){
                        for(i in 0..RetNama().size -1) {
//                            nama_sementara.add(RetNama().keys.elementAt(i))
//                            kode_sementara.add(RetKode().keys.elementAt(i))
                            arr_nama.add(RetNama().keys.elementAt(i))
                            arr_kode.add(RetKode().keys.elementAt(i))
                        }
                        arr_kode = kode_sementara
                        arr_nama = nama_sementara
                    }
                }


            }
        })
        recyclerview.adapter = adapter
        return adapter
    }
    override fun onResume() {
        super.onResume()
        adapter = getAdapter2()
        adapter.notifyDataSetChanged()
        recyclerview.adapter = adapter
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
            onBackPressedDispatcher.onBackPressed()
        }
    }
}