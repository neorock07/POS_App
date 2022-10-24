package com.example.pos.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.pos.R
import java.lang.NullPointerException

class formBarang : AppCompatActivity() {
private lateinit var dropmenu : AutoCompleteTextView
    private lateinit var arrayAdapter: ArrayAdapter <String>
    private lateinit var scan:ImageView
    private lateinit var ed_kode:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_barang)
        var item = arrayOf("Kertas","Alat Tulis","Buku", "Sampul","Kotak Pensil","Aksesoris", "Lainnya")
        //assign variable
        dropmenu = findViewById(R.id.jenis_barang)
        scan = findViewById(R.id.scan_code)
        ed_kode = findViewById(R.id.kode_barang)
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



    }
}