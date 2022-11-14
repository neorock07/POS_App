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
import com.example.pos.Database.Database
import com.example.pos.Model.model_barang
import com.example.pos.R


class Adapter_Dft_Barang(private var context: Context, private var modelitem: ArrayList<model_barang>
                         ):RecyclerView.Adapter<Adapter_Dft_Barang.MyViewHolder>() {
    private var listener: Adapter_Dft_Barang.OnItemsClickListener? = null
    public val arrayname = Array<String>(itemCount){""}
    fun setWhenClickListener(listener: Adapter_Dft_Barang.OnItemsClickListener?){
        this.listener = listener
    }
    val db:Database = Database(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.container_barang, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.kode.text = "KODE : " + modelitem.get(position).kode
        holder.jenis.text = modelitem.get(position).jenis
        holder.nama.text = modelitem.get(position).nama
        holder.stok.text = "Stok : " + NumberFormat(modelitem.get(position).stok.toString())
        holder.harga.text = "Rp"+  NumberFormat(modelitem.get(position).harga.toString())
        //button onclick to delete data
        holder.btn_delete.setOnClickListener{
            val alertDialog = AlertDialog.Builder(context)
                .setMessage("Anda yakin ingin menghapus data ini ?")
                .setIcon(R.drawable.warning)
                .setTitle("Konfirmasi")
                .setCancelable(true)
                .setPositiveButton("Yakin", DialogInterface.OnClickListener { dialogInterface, i ->
                    deleteBrg(modelitem.get(position).kode)
                    if(listener != null){
                        listener!!.onItemClick(true)
                    }
                })
                .setNegativeButton("Gak dulu", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                    Toast.makeText(context, "Woke!", Toast.LENGTH_SHORT).show()
                })
                .show()
        }
    }
    fun filterList(filterlist: ArrayList<model_barang>) {
        // below line is to add our filtered
        // list in our course array list.
        modelitem = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
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
         return modelitem!!.size
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val harga = itemView.findViewById<TextView>(R.id.harga_item)
        val jenis = itemView.findViewById<TextView>(R.id.jenis_item)
        val kode = itemView.findViewById<TextView>(R.id.kode_item)
        val nama = itemView.findViewById<TextView>(R.id.nama_item_brg)
        val stok = itemView.findViewById<TextView>(R.id.stok_item_brg)
        val btn_delete = itemView.findViewById<CardView>(R.id.btn_delete_brg)
    }
    interface OnItemsClickListener {
        fun onItemClick(refresh: Boolean)
    }
}