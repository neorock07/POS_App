package com.example.pos.RecycleView

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.database
import com.example.pos.R


class Adapter_Dft_Barang(private val context: Context,
                            private val list_kode:ArrayList<String>?,
                         private val list_jenis:ArrayList<String>?,
                         private val list_name:ArrayList<String>?,
                         private val list_harga:ArrayList<Int>?,
                         private val list_stok:ArrayList<Int>?
                         ):RecyclerView.Adapter<Adapter_Dft_Barang.MyViewHolder>() {

    val db:database = database(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.container_barang, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.kode.text = "KODE : " + list_kode!![position]
        holder.jenis.text = list_jenis!![position]
        holder.nama.text = list_name!![position]
        holder.stok.text = "Stok : " + NumberFormat(list_stok!![position].toString())
        holder.harga.text = "Rp"+  NumberFormat(list_harga!![position].toString())
        //button onclick to delete data
        holder.btn_delete.setOnClickListener{
            val alertDialog = AlertDialog.Builder(context)
                .setMessage("Anda yakin ingin menghapus data ini ?")
                .setIcon(R.drawable.warning)
                .setTitle("Konfirmasi")
                .setCancelable(true)
                .setPositiveButton("Yakin", DialogInterface.OnClickListener { dialogInterface, i ->
                    deleteBrg(list_kode!![position])
                })
                .setNegativeButton("Gak dulu", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                    Toast.makeText(context, "Woke!", Toast.LENGTH_SHORT).show()
                })
                .show()
        }
    }

    fun NumberFormat(s:String):String{
        var current:String = ""
        var parsed:Double
        var cleanString:String = s.toString().replace("""[,.]""".toRegex(), "")
        parsed = cleanString.toDouble()
        var formatted:String = java.text.NumberFormat.getNumberInstance().format(parsed)
        current = formatted
        return current
    }
    //delete data
    fun deleteBrg(id:String){
        val result = db.deleteData(id)
        Toast.makeText(context, "Berhasil menghapus data", Toast.LENGTH_SHORT).show()
    }


    override fun getItemCount(): Int {
         return list_kode!!.size
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val harga = itemView.findViewById<TextView>(R.id.harga_item)
        val jenis = itemView.findViewById<TextView>(R.id.jenis_item)
        val kode = itemView.findViewById<TextView>(R.id.kode_item)
        val nama = itemView.findViewById<TextView>(R.id.nama_item_brg)
        val stok = itemView.findViewById<TextView>(R.id.stok_item_brg)
        val btn_delete = itemView.findViewById<CardView>(R.id.btn_delete_brg)
    }

}