package com.example.pos.Activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.example.pos.Database.Database
import com.example.pos.R
import java.lang.NullPointerException
import java.text.NumberFormat

class Edit_Barang : AppCompatActivity() {
    private lateinit var edit_kode:EditText
    private lateinit var   edit_nama:EditText
    private lateinit var     edit_jenis: AutoCompleteTextView
    private lateinit var    edit_stok:EditText
    private lateinit var     edit_harga:EditText
    private  var     main:MainActivity= MainActivity()
    private var TABLE_CONTACTS = "Barang2"
    private var KEY_ID = "Kode"
    private val KEY_NAME = "Nama"
    private val KEY_HARGA = "Harga"
    private val KEY_JENIS = "Jenis"
    private val KEY_STOK = "Stok"
    private lateinit var ubah : Button
    private lateinit var code:String
    var nilai:Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_barang)
        val kembali: ImageView = findViewById(R.id.kembali)
        edit_kode = findViewById(R.id.edit_kode_barang)
         edit_nama= findViewById(R.id.edit_nama_barang)
         edit_jenis= findViewById(R.id.edit_jenis_barang)
         edit_stok = findViewById(R.id.edit_stok_barang)
         edit_harga= findViewById(R.id.edit_harga_barang)
         ubah = findViewById(R.id.ubah)
        kembali.setOnClickListener{
            onBackPressed()
        }
        edit_harga.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var current: String = ""

                if (!s.toString().equals(current)) {
                    edit_harga.removeTextChangedListener(this)
                    var cleanString: String = s.toString().replace("""[,.]""".toRegex(), "")
                    nilai = cleanString.toDouble()
                    var formatted: String = NumberFormat.getNumberInstance().format(nilai)
                    current = formatted
                    edit_harga.setText(formatted)
                    edit_harga.setSelection(formatted.length)
                    edit_harga.addTextChangedListener(this)

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        ubah.setOnClickListener(){
            updatedata()
            val intent = Intent(this, Edit_Barang::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun GetData(){
        code = intent.getStringExtra("key_kode")!!
        var jenis = intent.getStringExtra("key_jenis")
        var nama = intent.getStringExtra("key_nama")
        var harga = intent.getIntExtra("key_harga",0)
        var stok = intent.getIntExtra("key_stok",0)

        edit_kode.setText(code)
        edit_nama.setText(nama)
        edit_harga.setText(main.NumberFormat(harga.toString()))
        edit_stok.setText((main.NumberFormat(stok.toString())))
        edit_jenis.setText(jenis)
    }

    override fun onResume() {
        super.onResume()
        try{
            GetData()
        }catch (e:NullPointerException){
            Log.d("Null", e.message.toString())
        }

    }
    fun updatedata(){
        if(edit_kode.text.isEmpty()|| edit_harga.text.isEmpty() || edit_nama.text.isEmpty() || edit_stok.text.isEmpty() || edit_jenis.text.isEmpty()){
            edit_kode.error = "Tidak boleh kosong"
            edit_harga.error = "Tidak boleh kosong"
            edit_nama.error = "Tidak boleh kosong"
            edit_stok.error = "Tidak boleh kosong"
            edit_jenis.error = "Tidak boleh kosong"
        }
        else{
            val db: Database=Database(this)
            val dbhelper = db.writableDatabase
            val content : ContentValues= ContentValues()
            val hargaitem : Int = nilai.toInt()
            content.put(KEY_ID, edit_kode.text.toString())
            content.put(KEY_HARGA, hargaitem)
            content.put(KEY_JENIS, edit_jenis.text.toString())
            content.put(KEY_NAME, edit_nama.text.toString())
            content.put(KEY_STOK, edit_stok.text.toString().toInt())
            dbhelper.update(TABLE_CONTACTS,content,"$KEY_ID=?", arrayOf(code))
            Toast.makeText(this,"Data berhasil diubah", Toast.LENGTH_SHORT).show()
        }
    }
}
