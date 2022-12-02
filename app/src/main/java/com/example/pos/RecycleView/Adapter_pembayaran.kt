package com.example.pos.RecycleView

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Model.model_barang
import com.example.pos.R

class Adapter_pembayaran(
    private var context: Context,
    private var list_kode: ArrayList<String>?,
    private var list_nama: ArrayList<String>?,
    private var list_harga: ArrayList<Int>?,
    private var list_jenis: ArrayList<String>?,
    private var list_jumlah: ArrayList<Int>?
) : RecyclerView.Adapter<Adapter_pembayaran.MyHolder>() {
    private var listener: Adapter_pembayaran.OnItemsClickListener? = null
    val arr_jmlh = HashMap<String, Int>()
    fun setWhenClickListener(listener: OnItemsClickListener?) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.container_item_beli, parent, false)
        return MyHolder(v)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {

//        var model = model_item.get(position)
//        holder.namaBarang.text = model.nama
        holder.hargaBarang.text = "Rp." + NumberFormat(list_harga!!.get(position).toString())
        holder.jumlah.text = list_jumlah!!.get(position).toString()
        // sets the image to the imageview from our itemHolder class
        holder.jenisItem.text = list_jenis!!.get(position)
        // sets the text to the textview from our itemHolder class
        holder.namaBarang.text = list_nama!!.get(position)
        var num = holder.jumlah.text.toString().toInt()
        val hargaString = "Rp" + NumberFormat(list_harga!!.get(position).toString())
        holder.plusButton.setOnClickListener {
            if (listener != null) {
                    if(holder.jumlah.text.toString().toInt() >= 0){
                        num += 1
                        Toast.makeText(context, "num : $num", Toast.LENGTH_SHORT).show()
                        holder.jumlah.text = num.toString()
                        list_jumlah!!.set(position,num)
                    } else{
                        holder.jumlah.text = num.toString()
                    }
                    arr_jmlh[list_nama!!.get(position)] = holder.jumlah.text.toString().toInt()
                    listener!!.onItemClick(list_jumlah!!, refresh2(list_harga!!.get(position)))

            }
        }
        holder.minButton.setOnClickListener {
            if (listener != null) {
                if (holder.jumlah.text.toString() != "0") {
                    Toast.makeText(context, "Masih 0", Toast.LENGTH_SHORT).show()
                        if (holder.jumlah.text.toString().toInt() >= 0) {
                            num -= 1
                            Toast.makeText(context, "num : $num", Toast.LENGTH_SHORT).show()
                            holder.jumlah.text = num.toString()
                            list_jumlah!!.set(position,num)
                        } else {
                            holder.jumlah.text = num.toString()
                        }
                    arr_jmlh[list_nama!!.get(position)] = holder.jumlah.text.toString().toInt()
                        listener!!.onItemClick(list_jumlah!!, refresh3(list_harga!!.get(position)))
                }
            }
        }

    }

    fun NumberFormat(s: String): String {
        var current: String = ""
        var parsed: Double
        var cleanString: String = s.replace("""[,.]""".toRegex(), "")
        parsed = cleanString.toDouble()
        var formatted: String = java.text.NumberFormat.getNumberInstance().format(parsed)
        current = formatted
        return current
    }

    override fun getItemCount(): Int {
        return list_kode!!.size
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val jenisItem: TextView = itemView.findViewById(R.id.jenis_item)
        val namaBarang: TextView = itemView.findViewById(R.id.detail_item)
        val hargaBarang: TextView = itemView.findViewById(R.id.harga_item)
        val plusButton: CardView = itemView.findViewById(R.id.btn_add)
        val minButton: CardView = itemView.findViewById(R.id.btn_min)
        val jumlah: TextView = itemView.findViewById(R.id.jumlah_item)
        val stok: TextView = itemView.findViewById(R.id.stok)
    }
    interface OnItemsClickListener {
        fun onItemClick(jumlah: ArrayList<Int>, total_harga : Int)
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
}