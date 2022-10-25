package RecycleView

import Database.EmpModelClass
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.R

class CustomAdapter(context: Context, private val kode:ArrayList<String>,
                    private val nama:ArrayList<String>,
                    private val harga:ArrayList<Int>,
                    private val jenis:ArrayList<String>,
                    private val stok:ArrayList<Int>,
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

        val ItemsViewModel = kode[position]
        // sets the image to the imageview from our itemHolder class
        holder.jenisItem.text = jenis[position]
        // sets the text to the textview from our itemHolder class
        holder.namaBarang.text = nama[position]
        val hargaString = "Rp"+harga[position]

        holder.hargaBarang.text = hargaString

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