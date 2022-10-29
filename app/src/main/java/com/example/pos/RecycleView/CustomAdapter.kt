package com.example.pos.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.R

class CustomAdapter(context: Context, private val kode:ArrayList<String>?,
                    private val nama:ArrayList<String>?,
                    private val harga:ArrayList<Int>?,
                    private val jenis:ArrayList<String>?,
                    private val stok:ArrayList<Int>?,
                    ) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.container_item_beli, parent, false)
        return ViewHolder(view)

    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val kode_tx = kode!![position]
        // sets the image to the imageview from our itemHolder class
        holder.jenisItem.text = jenis!![position]
        // sets the text to the textview from our itemHolder class
        holder.namaBarang.text = nama!![position]
        val hargaString = "Rp."+ NumberFormat(harga!![position].toString())

        holder.hargaBarang.text = hargaString

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
        return kode!!.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val jenisItem: TextView = itemView.findViewById(R.id.jenis_item)
        val namaBarang: TextView = itemView.findViewById(R.id.detail_item)
        val hargaBarang: TextView = itemView.findViewById(R.id.harga_item)
    }
}