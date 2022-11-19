package com.example.pos.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.pos.R
import java.lang.Exception

class settingAplikasi : AppCompatActivity() {
    private lateinit var btn_rekap: CardView
    private lateinit var btn_katalog: CardView
//    private lateinit var kembali:ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_aplikasi)
        val kembali: ImageView = findViewById(R.id.kembali99)
        btn_rekap = findViewById(R.id.btn_rekap)
        btn_katalog = findViewById(R.id.btn_katalog)
        //threading

        kembali.setOnClickListener() {
            onBackPressed()
        }
        btn_katalog.setOnClickListener() {
            startActivity(Intent(this@settingAplikasi, daftar_barang::class.java))

        }

        btn_rekap.setOnClickListener{
            startActivity(Intent(this@settingAplikasi, Laporan_Tahunan::class.java))
        }

    }
}