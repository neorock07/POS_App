package com.example.pos.Activity

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
import com.example.pos.Model.model_bulan
import com.example.pos.Model.model_tahunan
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_Lpr_Bulanan
import com.example.pos.RecycleView.Adapter_Lpr_Tahunan
import org.w3c.dom.Text

class Laporan_Bulanan : AppCompatActivity() {
    private lateinit var  rc: RecyclerView
    private lateinit var adapter: Adapter_Lpr_Bulanan
    private  lateinit var list_data:ArrayList<model_bulan>
    private lateinit var db: Database
    private lateinit var tx_thn:TextView
    private lateinit var name_tahun: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_bulanan)
        //inisiasi variable
        tx_thn = findViewById(R.id.txt_tahun)
        rc = findViewById(R.id.rc_bulanan)
        rc.setHasFixedSize(true)
        rc.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        //get tahun from adapter tahunan
        name_tahun = intent.getStringExtra("Key_Tahun")!!
        tx_thn.text = name_tahun

    }

    private fun readDataMonth(tahun:String){
        list_data = ArrayList()
        db = Database(this)
        val cursor:Cursor = db.readIncomePerMonth(tahun)
        if(cursor.count > 0){
            while(cursor.moveToNext()){
                var bulan = cursor.getString(0)
                var income = cursor.getInt(1)
                list_data.add(
                    model_bulan(
                        name_tahun,
                        income,
                        bulan
                    )
                )
            }
        }
        Toast.makeText(this,"Isi : " + list_data.toString(), Toast.LENGTH_LONG).show()
        adapter = Adapter_Lpr_Bulanan(this, list_data)
        rc.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        readDataMonth(name_tahun)
    }
}