package com.example.pos.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Activity.MainActivity
import com.example.pos.Model.model_barang
import com.example.pos.R
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashSet

class CustomAdapter(private var context: Context, private var modelitem: ArrayList<model_barang>
                    ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
    private var listener: OnItemsClickListener? = null
    public val arrayname = Array<String>(itemCount){""}
    //data > 0 add to array
    var arr_kode = ArrayList<String>()
    var arr_harga = ArrayList<Int>()
    var arr_jenis = ArrayList<String>()
    var arr_nama = ArrayList<String>()
    var arr_jumlah:ArrayList<Int> = ArrayList()
    fun setWhenClickListener(listener: OnItemsClickListener?){
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
        val arr = IntArray(itemCount)

        val kode_tx = modelitem.get(position).kode
        // sets the image to the imageview from our itemHolder class
        holder.jenisItem.text = modelitem.get(position).jenis
        // sets the text to the textview from our itemHolder class
        holder.namaBarang.text = modelitem.get(position).nama
        val hargaString = "Rp"+ NumberFormat(modelitem.get(position).harga.toString())

        holder.hargaBarang.text = hargaString
        holder.minButton.setOnClickListener{
            if(listener != null){
                if (holder.jumlah.text.toString() != "0"){
                    arr[position] = --arr[position]
                    holder.jumlah.text = arr[position].toString()
                    listener!!.onItemClick(refresh2(modelitem.get(position).harga))

                    //listener!!.onArrayItemClick(arr_kode,arr_harga,arr_nama,arr_jenis, arr_jumlah)
                }
            }
                }
        holder.plusButton.setOnClickListener{
            val kode_lt = mutableListOf<String>(modelitem.get(position).kode)
            val kk = LinkedHashSet(kode_lt).toMutableList()
            val nama_lt = mutableListOf<String>(modelitem.get(position).nama)
            val k2 = LinkedHashSet(nama_lt).toMutableList()
            var jml:ArrayList<Int> = ArrayList()
            if(listener != null){
                arr[position] = ++arr[position]
                    holder.jumlah.text = arr[position].toString()
                    listener!!.onItemClick(modelitem.get(position).harga)

                if(arr[position] > 0 ){
                    for(i in kk){
                        arr_kode.add(i)
                    }
                    for(i in k2){
                        arr_nama.add(i)
                    }
                    Toast.makeText(context, "kode : " + kode_lt.toString(), Toast.LENGTH_SHORT).show()
                    arr_harga.add(modelitem.get(position).harga)
                    arr_jenis.add(modelitem.get(position).jenis)
                    jml.add(holder.jumlah.text.toString().toInt())
                  //  var mp = mapOf("1" to jml)
                    arr_jumlah.add(jml.get(jml.size - 1))
                    jml.clear()
//                    var kode_lt:ArrayList<String> = mutableListOf<Set<ArrayList<String>>>(mutableSetOf<ArrayList<String>>(arr_kode)) as ArrayList<String>
//                    var jenis_lt:ArrayList<String> = mutableListOf<Set<ArrayList<String>>>(mutableSetOf<ArrayList<String>>(arr_jenis)) as ArrayList<String>
//                    var nama_lt:ArrayList<String> = mutableListOf<Set<ArrayList<String>>>(mutableSetOf<ArrayList<String>>(arr_nama)) as ArrayList<String>
//                    var harga_lt:ArrayList<Int> = mutableListOf<Set<ArrayList<Int>>>(mutableSetOf<ArrayList<Int>>(arr_harga)) as ArrayList<Int>

                    //var jumlah_lt = mutableListOf<Int>(arr_jumlah[arr_jumlah.size - 1]) as ArrayList<String>
                    //var jumlah_lt = mutableListOf<Set<ArrayList<Int>>>(mutableSetOf<ArrayList<Int>>(arr_jumlah))
                }
                listener!!.onArrayItemClick(arr_kode,arr_harga,arr_nama,arr_jenis, arr_jumlah)
            }
            }


    }
    fun refresh2(harga : Int): Int{
        var x = 0
        x-=harga
        return x
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


    // return the number of the items in the list
    override fun getItemCount(): Int {
        return modelitem!!.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val jenisItem: TextView = itemView.findViewById(R.id.jenis_item)
        val namaBarang: TextView = itemView.findViewById(R.id.detail_item)
        val hargaBarang: TextView = itemView.findViewById(R.id.harga_item)
        val plusButton : CardView = itemView.findViewById(R.id.btn_add)
        val minButton : CardView = itemView.findViewById(R.id.btn_min)
        val jumlah: TextView = itemView.findViewById(R.id.jumlah_item)
    }
    interface OnItemsClickListener {
        fun onItemClick(harga: Int)
        fun onArrayItemClick(kode:ArrayList<String>, Arr_harga:ArrayList<Int>, nama:ArrayList<String>, jenis:ArrayList<String>, jumlah_total:ArrayList<Int>)
    }
}


