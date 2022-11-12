package com.example.pos.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_pembayaran
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.ImagePrintable
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import net.glxn.qrgen.android.QRCode
import java.nio.charset.Charset
import java.util.*
import kotlin.collections.ArrayList

//import com.mazenrashed.printooth.Printooth
//import com.mazenrashed.printooth.utilities.Printing

class Struk_Activity : AppCompatActivity() {
    private var print: Printing? =null
    private lateinit var btn_print:Button
    private lateinit var rc:RecyclerView
    private lateinit var rv:RelativeLayout
    private lateinit var btn_kembali:Button
    private lateinit var list_kode:ArrayList<String>
    private lateinit var list_nama:ArrayList<String>
    private lateinit var list_jenis:ArrayList<String>
    private lateinit var list_harga:ArrayList<Int>
    private lateinit var list_jumlah:ArrayList<Int>
    private lateinit var totalHarga:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_struk)

        Printooth.init(this)
        //assign variable
        val main:MainActivity = MainActivity()
        rv = findViewById(R.id.rv_empty)
        rc = findViewById(R.id.rc_item_cetak)
        btn_kembali = findViewById(R.id.btn_kembali)
        rc.setHasFixedSize(true)
        rc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        btn_print = findViewById(R.id.btn_cetak)
        //cek koneksi
        if(Printooth.hasPairedPrinter()){
            print = Printooth.printer()
        }

        //callback from printooth to get printer process
        print?.printingCallback = object : PrintingCallback{
            override fun connectingWithPrinter() {
                Toast.makeText(this@Struk_Activity, "Connecting with printer...",Toast.LENGTH_SHORT).show()
            }

            override fun connectionFailed(error: String) {
                Toast.makeText(this@Struk_Activity, "Failed to connect printer",Toast.LENGTH_SHORT).show()

            }

            override fun disconnected() {
                Toast.makeText(this@Struk_Activity, "Connection disconnect",Toast.LENGTH_SHORT).show()

            }

            override fun onError(error: String) {
                Toast.makeText(this@Struk_Activity, "getting an error : \n" + error,Toast.LENGTH_SHORT).show()
            }

            override fun onMessage(message: String) {
                Toast.makeText(this@Struk_Activity, "Message : \n" + message,Toast.LENGTH_SHORT).show()

            }

            override fun printingOrderSentSuccessfully() {
                Toast.makeText(this@Struk_Activity, "Order successfully sent to printer!",Toast.LENGTH_SHORT).show()
            }
        }
           //button kembali
        btn_kembali.setOnClickListener{
            onBackPressed()
            finish()
        }
    }
    private fun RetrieveData(){
        list_kode = intent.getSerializableExtra("key_kode") as ArrayList<String>
        list_nama = intent.getSerializableExtra("key_nama") as ArrayList<String>
        list_jenis = intent.getSerializableExtra("key_jenis") as ArrayList<String>
        list_harga = intent.getSerializableExtra("key_harga") as ArrayList<Int>
        list_jumlah = intent.getSerializableExtra("key_jumlah") as ArrayList<Int>
        totalHarga = intent.getStringExtra("key_total")!!

        if(list_kode.isEmpty()){
            rv.visibility = View.VISIBLE
        }else{
            val adapter:Adapter_pembayaran = Adapter_pembayaran(this@Struk_Activity, list_kode, list_nama,list_harga, list_jenis, list_jumlah)
            rc.adapter = adapter
        }

    }
    override fun onResume() {
        super.onResume()
        initListener()
        RetrieveData()
    }
    private fun initListener(){
        btn_print.setOnClickListener{
            if(!Printooth.hasPairedPrinter()){
                resultLaunh.launch(
                    Intent(
                        this@Struk_Activity,
                        ScanningActivity::class.java
                    ),
                )
            }else{
                printDetails()
            }
        }

    }
    private fun printDetails(){
        val printMsg = MsgPrint()
        print!!.print(printMsg)
    }

    private fun MsgPrint() =  ArrayList<Printable>().apply {
        val calender:Calendar = Calendar.getInstance()
        val jam = calender.get(Calendar.HOUR_OF_DAY)
        val menit = calender.get(Calendar.MINUTE)
        val mm = calender.get(Calendar.MILLISECOND)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val month = calender.get(Calendar.MONTH) + 1
        val year = calender.get(Calendar.YEAR)

        add(RawPrintable.Builder(byteArrayOf(27, 100, 4)).build())
        //logo
        add(ImagePrintable.Builder(R.drawable.logo, resources)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER).build())
        //cetak tulisan point of sales di tengah
        add(
            TextPrintable.Builder()
                .setText("Point of Sale")
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setFontSize(DefaultPrinter.FONT_SIZE_LARGE)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .setUnderlined(DefaultPrinter.UNDERLINED_MODE_OFF)
                .setNewLinesAfter(1)
                .build()
        )
        var n = 10
        val arr_id = ByteArray(256)
        val randomStr:String = String(arr_id, Charset.forName("UTF-8"))
        var strBuilder:StringBuilder = StringBuilder()
        for(i in (0..randomStr.length-1)){
            val ch = randomStr.get(i)
            if((ch >= 'a' &&  ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9') && (n > 0)){
                strBuilder.append(ch)
                n--
            }
        }
        strBuilder.toString()
        add(
            TextPrintable.Builder()
                .setText("ID Pemesanan\t: $strBuilder")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        add(
            TextPrintable.Builder()
                .setText("Tanggal\t\t: $day/$month/$year $jam:$menit:$mm")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        for(i in (0..list_kode.size -1)){
            add(
                TextPrintable.Builder()
                    .setText("$list_nama.get($i)\t\t: Rp$list_harga.get($i)")
                    .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                    .setNewLinesAfter(1)
                    .build()
            )
        }
        add(
            TextPrintable.Builder()
                .setText("========================================")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        add(
            TextPrintable.Builder()
                .setText("Total\t\t\t Rp $totalHarga")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(2)
                .build()
        )
        add(
            TextPrintable.Builder()
                .setText("Lunas")
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setFontSize(DefaultPrinter.FONT_SIZE_LARGE)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .setUnderlined(DefaultPrinter.UNDERLINED_MODE_OFF)
                .setNewLinesAfter(1)
                .build()
        )
        val qr:Bitmap = QRCode.from("ID Pemesanan : $strBuilder\n" +
                "Tanggal : $day/$month/$year $jam:$menit:$mm\n" + "Jumlah : Rp.$totalHarga\n\nQRCode made with POS App")
            .withSize(200,200).bitmap()

        add(
            ImagePrintable.Builder(qr)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .build()
        )
        add(
            TextPrintable.Builder()
                .setText("Made with POS App")
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setFontSize(DefaultPrinter.FONT_SIZE_LARGE)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .setUnderlined(DefaultPrinter.UNDERLINED_MODE_OFF)
                .setNewLinesAfter(1)
                .build()
        )
        add(
            RawPrintable.Builder(byteArrayOf(27,100,4)).build()
        )
    }
    var resultLaunh = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result -> if(result.resultCode == ScanningActivity.SCANNING_FOR_PRINTER && result.resultCode == Activity.RESULT_OK ){
            printDetails()
    }

    }
}



