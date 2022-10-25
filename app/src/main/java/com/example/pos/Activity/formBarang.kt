package com.example.pos.Activity

import Database.database
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.pos.R
import java.lang.NullPointerException

class formBarang : AppCompatActivity() {
private lateinit var dropmenu : AutoCompleteTextView
    private lateinit var arrayAdapter: ArrayAdapter <String>
    private lateinit var scan:ImageView
    private lateinit var ed_kode:EditText
    private lateinit var name :EditText
    private lateinit var harga :EditText
    private lateinit var stok :EditText
    private lateinit var kembali :ImageView
    private var TABLE_CONTACTS = "Barang2"
    private var KEY_ID = "Kode"
    private val KEY_NAME = "Nama"
    private val KEY_HARGA = "Harga"
    private val KEY_JENIS = "Jenis"
    private val KEY_STOK = "Stok"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_barang)
        var item = arrayOf("Kertas","Alat Tulis","Buku", "Sampul","Kotak Pensil","Aksesoris", "Lainnya")
        //assign variable
        dropmenu = findViewById(R.id.jenis_barang)
        scan = findViewById(R.id.scan_code)
        ed_kode = findViewById(R.id.kode_barang)
        name = findViewById(R.id.nama_barang)
        harga = findViewById(R.id.harga_barang)
        stok = findViewById(R.id.stok_barang)
        kembali = findViewById(R.id.kembali)
        //array adapter for dropdown menu
        arrayAdapter = ArrayAdapter(applicationContext,R.layout.dropdown_jenis,item)
        dropmenu.setAdapter(arrayAdapter)

        //go to scan
        scan.setOnClickListener(){
            startActivity(Intent(this@formBarang, Scanner_Activity::class.java))
        }
        //get data from scanner
        try{
            var data: String? = intent.extras!!.getString("kode")
            ed_kode!!.setText(data)

        }catch (e:NullPointerException){
            Toast.makeText(this, "unable to start\n"+ e.message, Toast.LENGTH_SHORT).show()
        }
        kembali.setOnClickListener {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
    fun saveRecord(view: View){
        val idString = ed_kode.text.toString()
        val nameString = name.text.toString()
        val hargaString = harga.text.toString().toInt()
        val jenisString= dropmenu.text.toString()
        val stokString = stok.text.toString().toInt()
        val databaseHandler: database = database(this)
        val db = databaseHandler.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(KEY_ID, idString)
        contentValues.put(KEY_NAME, nameString)
        contentValues.put(KEY_STOK, stokString)// EmpModelClass Name
        contentValues.put(KEY_HARGA, hargaString)
        contentValues.put(KEY_JENIS, jenisString)// EmpModelClass Phone
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, contentValues)
//        if(idString.trim()!="" && nameString.trim()!="" && hargaString.trim()!=""
//            && jenisString.trim()!=""
//            && stokString.trim()!=""){
//            val status = databaseHandler.addBarang(EmpModelClass(idString, nameString, Integer.parseInt(hargaString), jenisString, Integer.parseInt(stokString)))
//            if(status > -1 && jenisString != "Jenis Barang"){
                Toast.makeText(applicationContext,"record save", Toast.LENGTH_LONG).show()
                ed_kode.text.clear()
                name.text.clear()
                harga.text.clear()
                stok.text.clear()
                dropmenu.setText("Jenis Barang")

//            }
//        }else{
//            Toast.makeText(applicationContext,"Input tidak boleh kosong", Toast.LENGTH_LONG).show()
//        }

    }
}