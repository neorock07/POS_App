package com.example.pos.Activity

import com.example.pos.Database.database
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.example.pos.R
import java.io.IOException
import java.lang.NullPointerException
import java.lang.NumberFormatException
import java.text.NumberFormat

class formBarang : AppCompatActivity() {
private lateinit var dropmenu : AutoCompleteTextView
    private lateinit var arrayAdapter: ArrayAdapter <String>
    private lateinit var scan:ImageView
    private lateinit var ed_kode:EditText
    private lateinit var name :EditText
    private lateinit var harga :EditText
    private lateinit var stok :EditText
    private lateinit var kembali :ImageView
    private lateinit var btn_simpan:Button
    private var TABLE_CONTACTS = "Barang2"
    private var KEY_ID = "Kode"
    private val KEY_NAME = "Nama"
    private val KEY_HARGA = "Harga"
    private val KEY_JENIS = "Jenis"
    private val KEY_STOK = "Stok"
    private var parsed:Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_barang)
        var item = arrayOf("Kertas","Alat Tulis","Buku", "Sampul","Kotak Pensil","Aksesoris", "Makanan", "Minuman","Alat Mandi", "Lainnya")
        //assign variable
        dropmenu = findViewById(R.id.jenis_barang)
        scan = findViewById(R.id.scan_code)
        ed_kode = findViewById(R.id.kode_barang)
        name = findViewById(R.id.nama_barang)
        harga = findViewById(R.id.harga_barang)
        stok = findViewById(R.id.stok_barang)
        kembali = findViewById(R.id.kembali)
        btn_simpan = findViewById(R.id.simpan)

        //array adapter for dropdown menu
        arrayAdapter = ArrayAdapter(applicationContext,R.layout.dropdown_jenis,item)
        dropmenu.setAdapter(arrayAdapter)

        //go to scan
        scan.setOnClickListener(){
            startActivity(Intent(this@formBarang, Scanner_Activity::class.java))
            finish()
        }
        //get data from scanner
        try{
            var data: String? = intent.extras!!.getString("kode")
            ed_kode!!.setText(data)

        }catch (e:NullPointerException){
            Toast.makeText(this, "Memulai\n"+ e.message, Toast.LENGTH_SHORT).show()
        }

        //btn kembali
        kembali.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        //formatting harga
        harga.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                var current:String =""

                if(!p0.toString().equals(current)){
                    harga.removeTextChangedListener(this)
                    var cleanString:String = p0.toString().replace("""[,.]""".toRegex(), "")
                    parsed = cleanString.toDouble()
                    var formatted:String = NumberFormat.getNumberInstance().format(parsed)
                    current = formatted
                    harga.setText(formatted)
                    harga.setSelection(formatted.length)
                    harga.addTextChangedListener(this)

                }
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
        //btn simpan
        btn_simpan.setOnClickListener(){
            try{
                saveRecord()
            }catch (e:Exception){
                Toast.makeText(this, "Error : Data tidak valid\nParsed : \n" + parsed + e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun saveRecord(){

        // Inserting Row
        if(ed_kode.text.isEmpty() || name.text.isEmpty() || harga.text.isEmpty() || stok.text.isEmpty() || dropmenu.text.equals("Jenis Barang")){
            ed_kode.error = "Tidak boleh kosong!"
            name.error = "Tidak boleh kosong!"
            harga.error = "Tidak boleh kosong!"
            stok.error = "Tidak boleh kosong!"
            dropmenu.error = "Jenis tidak valid"
        }else{

            val idString = ed_kode.text.toString()
            val nameString = name.text.toString()
            val jenisString= dropmenu.text.toString()
            val stokString = stok.text.toString().toInt()
            val databaseHandler: database = database(this)
            val db = databaseHandler.writableDatabase
            var contentValues = ContentValues()
            contentValues.put(KEY_ID, idString)
            contentValues.put(KEY_NAME, nameString)
            contentValues.put(KEY_STOK, stokString)// EmpModelClass Name
            contentValues.put(KEY_JENIS, jenisString)// EmpModelClass Phone

            val hargaString = parsed!!.toInt()
            contentValues.put(KEY_HARGA, hargaString)

            //insert data
            db.insert(TABLE_CONTACTS, null, contentValues)
            Toast.makeText(applicationContext,"record save", Toast.LENGTH_LONG).show()
            ed_kode.text.clear()
            name.text.clear()
            harga.text.clear()
            stok.text.clear()
            dropmenu.setText("Jenis Barang")
            val intent = Intent(this@formBarang, daftar_barang::class.java)
            startActivity(intent)
            finish()
        }
    }
}