package com.example.pos.RecycleView

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Model.model_barang
import com.example.pos.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashSet

class CustomAdapter(
    private var context: Context, private var modelitem: ArrayList<model_barang>,private var jml:HashMap<String, Int>?,private var jmlStok:HashMap<String, Int>?
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private var listener: OnItemsClickListener? = null
    //private var jumlah_item_select:ArrayList<Int> = ArrayList()
    val arrayname = Array<String>(itemCount) { "" }

    //data > 0 add to array
    var pos_item = ArrayList<Int>()
    var arr_kode = ArrayList<String>()
    var arr_harga =  ArrayList<Int>()
    var arr_jenis =  ArrayList<String>()
    var arr_nama = ArrayList<String>()
    val arr_jmlh =  ArrayList<Int>()
    val arr_stok =  ArrayList<Int>()



    fun setWhenClickListener(listener: OnItemsClickListener?) {
        this.listener = listener
    }

    // create new viewsbhg
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.container_item_beli, parent, false)
        return ViewHolder(view)

    }

    fun filterList(filterlist: ArrayList<model_barang>) {
        // below line is to add our filtered
        // list in our course array list.
        modelitem = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    // binds the list items to a viewd3
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        for (i in 0 until itemCount){
            arr_kode.add(modelitem.get(i).kode)
            arr_harga.add(modelitem.get(i).harga)
            arr_jenis.add(modelitem.get(i).jenis)
            arr_nama.add(modelitem.get(i).nama)
            arr_jmlh.add(modelitem.get(i).jumlah)
            arr_stok.add(modelitem.get(i).stok)
        }

        // sets the image to the imageview from our itemHolder class
        holder.jenisItem.text = modelitem.get(position).jenis
        // sets the text to the textview from our itemHolder class
        holder.namaBarang.text = modelitem.get(position).nama
        val hargaString = "Rp" + NumberFormat(modelitem.get(position).harga.toString())
        holder.hargaBarang.text = hargaString
        holder.stok.text = "Stok : "+ NumberFormat(modelitem[position].stok.toString())

        // holder.jumlah.text = jumlah_item_select.get(position).toString()

        //untuk menampilkan jumlah hasil intent dari pembayaran
        if(jml!!.isEmpty()){
            holder.jumlah.text = "0"
        }else{
            if(jml!![modelitem.get(position).nama] == null || jml!![modelitem.get(position).nama].toString().toInt() < 0){
                holder.jumlah.text = "0"
            }else{
                holder.jumlah.text = jml!![modelitem.get(position).nama].toString()
                arr_jmlh[position] = jml!![modelitem.get(position).nama]!!
            }

        }

        //untuk menampilkan stok hasil intent dari pembayaran
        if(jmlStok!!.isEmpty()){
            holder.stok.text = "Stok : " +  modelitem.get(position).stok.toString()
        }else{
            if(jmlStok!![modelitem.get(position).nama] == null || jmlStok!![modelitem.get(position).nama].toString().toInt() < 0){
                holder.stok.text = "Stok : " + modelitem.get(position).stok.toString()
            }else{
                holder.stok.text = "Stok : " + jmlStok!![modelitem.get(position).nama].toString()
                arr_stok[position] = jmlStok!![modelitem.get(position).nama]!!
            }
        }
        var num = holder.jumlah.text.toString().toInt()
        //minus button
        holder.minButton.setOnClickListener {
            if (listener != null || listener == null) {
                if (holder.jumlah.text.toString() != "0") {
                    Toast.makeText(context, "Masih 0",Toast.LENGTH_SHORT).show()
                        num -= 1
                        Toast.makeText(context, "num : $num",Toast.LENGTH_SHORT).show()
                        holder.jumlah.text = num.toString()
                        arr_stok[position]++
                    listener!!.onItemClick(refresh3(modelitem.get(position).harga))
                    }
                    else{
//                    holder.jumlah.text = arr[position].toString()
                        holder.jumlah.text = num.toString()
                    }

                    //stok
                    holder.stok.text = "Stok : " + arr_stok[position].toString()
                        arr_jmlh[position] = holder.jumlah.text.toString().toInt()

                        listener!!.onArrayItemClick(
                            arr_kode,
                            arr_harga,
                            arr_nama,
                            arr_jenis,
                            arr_jmlh,
                            arr_stok
                        )

                    }
                }

        //add button
        holder.plusButton.setOnClickListener {
            if (listener != null) {
                if (holder.stok.text.toString() != "Stok : 0"){
//                holder.jumlah.text = arr[position].toString()
//                listener!!.onItemClick(modelitem.get(position).harga)
                    num += 1
                    Toast.makeText(context, "num : $num",Toast.LENGTH_SHORT).show()
                    holder.jumlah.text = num.toString()
                    arr_stok[position]--
                    listener!!.onItemClick(refresh2(modelitem.get(position).harga))
                }
                else{
                    holder.jumlah.text = num.toString()
                }

                holder.stok.text = "Stok : " + NumberFormat(arr_stok[position].toString())

//                    //hashMap
                    arr_jmlh[position] = holder.jumlah.text.toString().toInt()

                    //Toast.makeText(context, "key : " + arr_jmlh, Toast.LENGTH_SHORT).show()
                }
                //              listener!!.getItemOnPosition(arr_jmlh,pos_item)
                listener!!.onArrayItemClick(
                    arr_kode,
                    arr_harga,
                    arr_nama,
                    arr_jenis,
                    arr_jmlh,
                    arr_stok
                )
            }
    }

    fun refresh2(harga: Int): Int {
        var x = 0
        x += harga
        return x
    }
    fun refresh3(harga: Int): Int {
        var x = 0
        x -= harga
        return x
    }

    fun NumberFormat(s: String): String {
        var current: String = ""
        var parsed: Double
        var cleanString: String = s.toString().replace("""[,.]""".toRegex(), "")
        parsed = cleanString.toDouble()
        var formatted: String = java.text.NumberFormat.getNumberInstance().format(parsed)
        current = formatted
        return current
    }
    // return the number of the items in the list
    override fun getItemCount(): Int {
        return modelitem!!.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

        val jenisItem: TextView     = itemView.findViewById(R.id.jenis_item)
        val namaBarang: TextView    = itemView.findViewById(R.id.detail_item)
        val hargaBarang: TextView   = itemView.findViewById(R.id.harga_item)
        val plusButton: CardView    = itemView.findViewById(R.id.btn_add)
        val minButton: CardView     = itemView.findViewById(R.id.btn_min)
        val jumlah: TextView        = itemView.findViewById(R.id.jumlah_item)
        val stok: TextView          = itemView.findViewById(R.id.stok)

    }

    interface OnItemsClickListener {
        fun onItemClick(harga: Int)
        fun onArrayItemClick(
            kode: ArrayList<String>,
            Arr_harga: ArrayList<Int>,
            nama: ArrayList<String>,
            jenis: ArrayList<String>,
            arr_jmlh: ArrayList<Int>,
            arr_stok: ArrayList<Int>

        )
//        fun getJumlahItem(jml:HashMap<String,Int>)
//        fun  getItemOnPosition(jml:HashMap<String,Int>,pos:ArrayList<Int>)

    }
}


