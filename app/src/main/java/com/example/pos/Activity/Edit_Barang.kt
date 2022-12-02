package com.example.pos.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.pos.R

class Edit_Barang : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_barang)
        val kembali: ImageView = findViewById(R.id.kembali)
        kembali.setOnClickListener{
            onBackPressed()
        }
    }
}
