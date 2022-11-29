package com.example.pos.RecycleView

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
import com.example.pos.Model.model_harian
import com.example.pos.Model.model_harian_detail
import com.example.pos.Model.model_tahunan
import com.example.pos.R
private lateinit var adapter: Adapter_Dft_Harian_Detail
private lateinit var db: Database
private lateinit var list_data2: ArrayList<model_harian_detail>
class Adapter_Lpr_Harian(private val context: Context, private var list_data:ArrayList<model_harian>?):
    RecyclerView.Adapter<Adapter_Lpr_Harian.MyHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adapter_Lpr_Harian.MyHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(R.layout.container_harian,parent,false)
        return Adapter_Lpr_Harian.MyHolder(v)
    }

    override fun onBindViewHolder(holder: Adapter_Lpr_Harian.MyHolder, position: Int) {
        val parentItem = list_data?.get(position)
        val layoutManager = LinearLayoutManager(
            holder.ChildRecyclerView
                .context,
            LinearLayoutManager.VERTICAL,
            false
        )
        if (parentItem != null) {
            layoutManager.initialPrefetchItemCount = parentItem
                .getChildItemList()
                .size
        }
        val childItemAdapter = Adapter_Dft_Harian_Detail(
            parentItem
                ?.getChildItemList()
        )

        holder.ChildRecyclerView.layoutManager = layoutManager
        holder.ChildRecyclerView.adapter = childItemAdapter
        holder.ChildRecyclerView
            .setRecycledViewPool(viewPool)
        holder.txt_bulan.text = list_data!!.get(position).bulan
        holder.txt_tanggal.text = list_data!!.get(position).tanggal

    }
    override fun getItemCount(): Int {
        return list_data!!.size
    }
    class MyHolder(val itemView: View):RecyclerView.ViewHolder(itemView) {
        val ChildRecyclerView = itemView.findViewById<RecyclerView>(R.id.detail_laporan)
        val txt_bulan = itemView.findViewById<TextView>(R.id.txt_bulan)
        val txt_tanggal = itemView.findViewById<TextView>(R.id.txt_tanggal)
    }
//    private fun readDataDay1(tanggal: String, tahun: String, bulan: String){
//        list_data2 = ArrayList()
//        db = Database(context)
//        val cursor: Cursor = db.readPerDetail(tanggal, tahun, bulan)
//        if(cursor.count > 0){
//            while(cursor.moveToNext()){
//                var nama = cursor.getString(0)
//                var harga = cursor.getString(1)
//                var jenis = cursor.getString(2)
//                var jumlah = cursor.getString(3)
//
//                list_data2.add(
//                    model_harian_detail(
//                        nama,harga,jenis,jumlah.toInt()
//                    )
//                )
//            }
//        }
//    }

}