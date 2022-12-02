package com.example.pos.Activity

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
import com.example.pos.Model.model_tahunan
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_Lpr_Tahunan

class Laporan_Tahunan : AppCompatActivity() {

    private lateinit var  rc:RecyclerView
    private lateinit var adapter:Adapter_Lpr_Tahunan
    private  lateinit var list_data:ArrayList<model_tahunan>
    private lateinit var db:Database
    private lateinit var kembali: ImageView
    var tahun:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_tahunan)

        //inisiasi variable
        rc = findViewById(R.id.laporan_tahunan)
        rc.setHasFixedSize(true)
        rc.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
        kembali = findViewById(R.id.kembali)

        kembali.setOnClickListener{
            onBackPressed()
        }
    }
    private fun RetData(){
        db = Database(this)
        var cursor: Cursor = db.readIncomePerYear()
        list_data = ArrayList()

        if(cursor.count > 0){
            while(cursor.moveToNext()){
                tahun = cursor.getString(0)
                if(tahun == null){
                    continue
                }else{
                    var income = cursor.getInt(1)
                    list_data.add(
                        model_tahunan(
                            tahun, income
                        )
                    )
                }

            }
        }
        Toast.makeText(this, "Data : ${list_data.toString()}",Toast.LENGTH_LONG).show()
        adapter = Adapter_Lpr_Tahunan(this, list_data)
        rc.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
            try{
                RetData()
            }catch(e:Exception){
                Toast.makeText(this, "Data : ${list_data.toString()}\nerror : ${e.message}",Toast.LENGTH_LONG).show()
            }
    }
}