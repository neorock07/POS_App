package com.example.pos.Activity

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_laporan_tahunan)

        //inisiasi variable
        rc = findViewById(R.id.laporan_tahunan)
        rc.setHasFixedSize(true)
        rc.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)


    }
    private fun RetData(){
        db = Database(this)
        var cursor: Cursor = db.readIncomePerYear()
        list_data = ArrayList()

        if(cursor.count > 0){
            while(cursor.moveToNext()){
                var tahun = cursor.getString(0)
                var income = cursor.getInt(1)
                list_data.add(
                    model_tahunan(
                        tahun, income
                    )
                )
            }
        }
        adapter = Adapter_Lpr_Tahunan(this, list_data)
        rc.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        RetData()
    }


}