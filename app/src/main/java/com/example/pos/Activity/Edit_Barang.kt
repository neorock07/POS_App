package com.example.pos.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.addTextChangedListener
import com.example.pos.R
import java.text.NumberFormat

class Edit_Barang : AppCompatActivity() {
    private lateinit var edit_kode:EditText
    private lateinit var   edit_nama:EditText
    private lateinit var     edit_jenis: AutoCompleteTextView
    private lateinit var    edit_stok:EditText
    private lateinit var     edit_harga:EditText
    private  var     main:MainActivity= MainActivity()
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
    }
    fun GetData(){
        var code = intent.getStringExtra("key_kode")
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
        GetData()
    }
}
