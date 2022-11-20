package com.example.pos.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
import com.example.pos.Model.model_barang
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_pembayaran
import com.google.android.material.textfield.TextInputEditText
import java.text.NumberFormat

class Pembayaran : AppCompatActivity() {
    lateinit var btn_cetak:ImageView
    lateinit var rc:RecyclerView
    lateinit var adapter:Adapter_pembayaran
    lateinit var total_beli_pem:TextView
    var total_uang:Int = 0
    lateinit var rv:RelativeLayout
    lateinit var ed_total:TextInputEditText
    var parsed:Double = 0.0
    var jumlah = 0
    var bayarTotal = ""
     var formatted:String=""
     var formatted2:String=""
    var kembalianHehe = ""
    val mainActivity:MainActivity = MainActivity()
    lateinit var kembalian:TextView
    lateinit var log2:Intent
    var hasil:Double =0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)
        log2 = Intent(this@Pembayaran, Struk_Activity::class.java)

        //assign variable
        total_beli_pem = findViewById(R.id.total_beli_pembayaran)
        btn_cetak = findViewById(R.id.to_cetak)
        rc = findViewById(R.id.rc_pembayaran)
        rv = findViewById(R.id.rv_empty)
        kembalian = findViewById(R.id.txt_kembalian)
        ed_total = findViewById(R.id.ed_total_beli)
        //recycleView
        rc.setHasFixedSize(true)
        rc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        ed_total.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(!p0!!.isEmpty()){
                    ed_total.removeTextChangedListener(this)
                    var cleanString:String = p0.toString().replace("""[,.]""".toRegex(), "")
                    parsed = cleanString.toDouble()
                    formatted = NumberFormat.getNumberInstance().format(parsed)

                    ed_total.setText(formatted)
                    ed_total.setSelection(formatted.length)
                    log2.putExtra("key_bayar", formatted.toString())
                    ed_total.addTextChangedListener(this)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                hasil = parsed - jumlah
                formatted2 = NumberFormat.getNumberInstance().format(hasil)
                if(hasil > 0){
                    kembalian.setText(mainActivity.NumberFormat(formatted2))
                    log2.putExtra("key_return",kembalian.text.toString())
                }else{
                    kembalian.setText("0")
                    log2.putExtra("key_return",kembalian.text.toString())
                }
            }
        }
        )

        RetrieveData()
    }
    fun RetrieveData():ArrayList<model_barang>{

        lateinit var listItem:ArrayList<model_barang>
        var list_kode:ArrayList<String> = intent.getSerializableExtra("key_kode") as ArrayList<String>
        var kode_lt = LinkedHashSet(list_kode).toMutableSet()
        jumlah = intent.getIntExtra("key_uang",0)
        val list_harga :HashMap<String, Int>?= intent.getSerializableExtra("key_harga") as HashMap<String, Int>?
        val list_nama:ArrayList<String> = intent.getSerializableExtra("key_nama") as ArrayList<String>

        val nama_lt = LinkedHashSet(list_nama).toMutableSet()
        val list_jenis= intent.getSerializableExtra("key_jenis") as HashMap<String, String>?
        val list_jml = intent.getSerializableExtra("key_jumlah") as HashMap<String, Int>?

        val hargaList:ArrayList<Int> = ArrayList()
        val jenisList:ArrayList<String> = ArrayList()
        val jumlahList:ArrayList<Int> = ArrayList()
        val list_kode2:ArrayList<String> = ArrayList()
        val list_nama2:ArrayList<String> = ArrayList()

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

        total_beli_pem.text = "Rp." + mainActivity.NumberFormat(jumlah.toString())

        listItem = ArrayList()
        if(list_kode2.isEmpty()){
            total_beli_pem.text = "Rp.0"
            rv.visibility = View.VISIBLE
        }else{
            adapter = Adapter_pembayaran(this@Pembayaran,list_kode2, list_nama2,hargaList, jenisList, jumlahList)
            rc.adapter = adapter
        }

        //go to halaman cetak struk

        log2.putExtra("key_kode", list_kode2)
        log2.putExtra("key_nama", list_nama2)
        log2.putExtra("key_harga", hargaList)
        log2.putExtra("key_jenis", jenisList)
        log2.putExtra("key_jumlah", jumlahList)

        log2.putExtra("key_total", mainActivity.NumberFormat(jumlah.toString()))
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