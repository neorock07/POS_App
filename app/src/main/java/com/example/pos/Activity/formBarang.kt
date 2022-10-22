package com.example.pos.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.pos.R

class formBarang : AppCompatActivity() {
private lateinit var dropmenu : AutoCompleteTextView
    private lateinit var arrayAdapter: ArrayAdapter <String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_barang)
        var item = arrayOf("Kertas","Alat Tulis","Buku", "Sampul","Kotak Pensil","Aksesoris", "Lainnya")
        dropmenu = findViewById(R.id.jenis_barang)
        arrayAdapter = ArrayAdapter(applicationContext,R.layout.dropdown_jenis,item)
        dropmenu.setAdapter(arrayAdapter)

    }
}