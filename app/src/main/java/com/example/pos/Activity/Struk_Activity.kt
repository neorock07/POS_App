package com.example.pos.Activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.Database.Database
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
    private var printCallback:PrintingCallback? = null
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
    private lateinit var bayar:String
    private lateinit var kembali:String

    val main:MainActivity = MainActivity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_struk)

        Printooth.init(this)
        //assign variable
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

        initView()
        initListener()

           //button kembali
        btn_kembali.setOnClickListener{
            onBackPressed()
            finish()
        }
        btn_print.setOnClickListener {
            btnPrint()
        }
    }

    private fun initListener() {
        //callback from printooth to get printer process
        if (print != null && printCallback == null) {
            printCallback = object : PrintingCallback {
                override fun connectingWithPrinter() {
                    Toast.makeText(
                        this@Struk_Activity,
                        "Connecting with printer...",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                override fun connectionFailed(error: String) {
                    Toast.makeText(this@Struk_Activity, "Failed to connect", Toast.LENGTH_SHORT)
                        .show()

                }

                override fun disconnected() {
                    Toast.makeText(
                        this@Struk_Activity,
                        "Disconnected with printer",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                override fun onError(error: String) {
                    Toast.makeText(this@Struk_Activity, "Error occured", Toast.LENGTH_SHORT).show()

                }

                override fun onMessage(message: String) {
                    Toast.makeText(this@Struk_Activity, "Msg: $message", Toast.LENGTH_SHORT).show()

                }

                override fun printingOrderSentSuccessfully() {
                    Toast.makeText(
                        this@Struk_Activity,
                        "Succesfully Sending data...",
                        Toast.LENGTH_SHORT
                    ).show()

                }

            }
            Printooth.printer().printingCallback = printCallback
        }
    }
//        print?.printingCallback = object : PrintingCallback{
//            override fun connectingWithPrinter() {
//                Toast.makeText(this@Struk_Activity, "Connecting with printer...",Toast.LENGTH_SHORT).show()
//            }
//
//            override fun connectionFailed(error: String) {
//                Toast.makeText(this@Struk_Activity, "Failed to connect printer",Toast.LENGTH_SHORT).show()
//
//            }
//
//            override fun disconnected() {
//                Toast.makeText(this@Struk_Activity, "Connection disconnect",Toast.LENGTH_SHORT).show()
//
//            }
//
//            override fun onError(error: String) {
//                Toast.makeText(this@Struk_Activity, "getting an error : \n" + error,Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onMessage(message: String) {
//                Toast.makeText(this@Struk_Activity, "Message : \n" + message,Toast.LENGTH_SHORT).show()
//
//            }
//
//            override fun printingOrderSentSuccessfully() {
//                Toast.makeText(this@Struk_Activity, "Order successfully sent to printer!",Toast.LENGTH_SHORT).show()
//            }
//        }

    private fun RetrieveData(){
        list_kode = intent.getSerializableExtra("key_kode") as ArrayList<String>
        list_nama = intent.getSerializableExtra("key_nama") as ArrayList<String>
        list_jenis = intent.getSerializableExtra("key_jenis") as ArrayList<String>
        list_harga = intent.getSerializableExtra("key_harga") as ArrayList<Int>
        list_jumlah = intent.getSerializableExtra("key_jumlah") as ArrayList<Int>
        totalHarga = intent.getStringExtra("key_total")!!
        bayar = intent.getStringExtra("key_bayar")!!
        kembali = intent.getStringExtra("key_return")!!

        Toast.makeText(this, "Bayar : $bayar\nKembali : $kembali",Toast.LENGTH_LONG).show()

        if(list_kode.isEmpty()){
            rv.visibility = View.VISIBLE
        }else{
            val adapter:Adapter_pembayaran = Adapter_pembayaran(this@Struk_Activity, list_kode, list_nama,list_harga, list_jenis, list_jumlah)
            rc.adapter = adapter
        }
    }

    fun InsertDataPembelian(){
        val calender:Calendar = Calendar.getInstance()
        val db:Database = Database(this)
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val month  = calender.get(Calendar.MONTH) + 1
            val year = calender.get(Calendar.YEAR)
            val date:String = "$year-$month-$day"
        try{
            for(i in 0..list_kode.size -1){
                db.insertDataPembelian(list_kode[i],list_jumlah[i],list_jumlah[i]*list_harga[i],date)
            }
        }catch (e:Exception){
            Toast.makeText(this, "Error + ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun btnPrint(){
        if(!Printooth.hasPairedPrinter()){
            startActivityForResult(Intent(this@Struk_Activity, ScanningActivity::class.java),ScanningActivity.SCANNING_FOR_PRINTER)
        }else{
            printMsg()
        }
    }

    override fun onResume() {
        super.onResume()
        initListener()
        RetrieveData()
    }
//    private fun InitListener(){
//        btn_print.setOnClickListener{
//            if(!Printooth.hasPairedPrinter()){
//                resultLaunh.launch(
//                    Intent(
//                        this@Struk_Activity,
//                        ScanningActivity::class.java
//                    ),
//                )
//            }else{
//                printDetails()
//            }
//
//            //printDetails()
//            InsertDataPembelian()
//        }
//
//    }
//    private fun printDetails(){
//        val printMsg = MsgPrint()
//        try{
//            print!!.print(printMsg)
//        }catch(e:Exception){
//            Log.d("Eror print : ", "Null pointer")
//        }
//    }

    private fun MsgPrint():ArrayList<Printable>{
        var al:ArrayList<Printable> = ArrayList()
        var calender: Calendar = Calendar.getInstance()
        val jam = calender.get(Calendar.HOUR_OF_DAY)
        val menit = calender.get(Calendar.MINUTE)
        val mm = calender.get(Calendar.MILLISECOND)
        val day = calender.get(Calendar.DAY_OF_MONTH)
        val month = calender.get(Calendar.MONTH) + 1
        val year = calender.get(Calendar.YEAR)

        al.add(RawPrintable.Builder(byteArrayOf(27, 100, 4)).build())
//        //logo
        al.add(
            TextPrintable.Builder()
                .setText("Invoice")
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setFontSize(DefaultPrinter.FONT_SIZE_LARGE)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .setUnderlined(DefaultPrinter.UNDERLINED_MODE_OFF)
                .setNewLinesAfter(1)
                .build()
        )

        val rnm = (1111111..9999999).random()
        al.add(
            TextPrintable.Builder()
                .setText("ID\t: $rnm")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        al.add(
            TextPrintable.Builder()
                .setText("Tanggal\t: $day/$month/$year $jam:$menit:$mm")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        al.add(
            TextPrintable.Builder()
                .setText("Item\t:")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        lateinit var value:String
        var valuePrice:String =""
        var jml:String=""
        for(i in (0..list_kode.size -1)){
                value = list_nama.get(i)
                valuePrice = list_harga.get(i).toString()
                jml = list_jumlah.get(i).toString()
                val total = jml.toInt() * valuePrice.toInt()
            al.add(
                TextPrintable.Builder()
                    .setText("$value\n@$valuePrice x$jml : Rp ${main.NumberFormat(total.toString())}\n" +
                            "-------------------------------")
                    .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                    .setNewLinesAfter(1)
                    .build()
            )
        }

        al.add(
            TextPrintable.Builder()
                .setText("===============================")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        al.add(
            TextPrintable.Builder()
                .setText("Total\t\t Rp $totalHarga")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        al.add(
            TextPrintable.Builder()
                .setText("Bayar\t\t Rp $bayar")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        al.add(
            TextPrintable.Builder()
                .setText("Kembali\t\t Rp $kembali")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(2)
                .build()
        )
        al.add(
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
        al.add(
            TextPrintable.Builder()
                .setText("*Simpan Struk Sebagai Bukti*")
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setFontSize(DefaultPrinter.FONT_SIZE_NORMAL)
                .setUnderlined(DefaultPrinter.UNDERLINED_MODE_OFF)
                .setNewLinesAfter(1)
                .build()
        )
        val qr:Bitmap = QRCode.from("ID\t: $rnm\n" +
                "Tanggal\t: $day/$month/$year $jam:$menit:$mm\n" + "Total\t: Rp.${main.NumberFormat(totalHarga)}\n\nQRCode made with POS App")
            .withSize(200,200).bitmap()

        al.add(
            ImagePrintable.Builder(qr)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .build()
        )

        al.add(
            TextPrintable.Builder()
                .setText("Powered with POS App")
                .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .setFontSize(DefaultPrinter.FONT_SIZE_NORMAL)
                .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
                .setUnderlined(DefaultPrinter.UNDERLINED_MODE_OFF)
                .setNewLinesAfter(1)
                .build()
        )

        al.add(
            RawPrintable.Builder(byteArrayOf(27,100,4)).build()
        )
        return al
    }
//    var resultLaunh = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//        result -> if(result.resultCode == ScanningActivity.SCANNING_FOR_PRINTER && result.resultCode == Activity.RESULT_OK ){
//            printDetails()
//    }
//
//    }
    fun printMsg(){
    if(print != null){
        print!!.print(MsgPrint())
    }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("Msg : ", "onActivityResult " + requestCode)
        if(requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK ){
            initListener()
            printMsg()
        }
        initView()
    }

    private fun initView() {
        if(Printooth.getPairedPrinter() != null){
            Toast.makeText(this, "Pair with " + Printooth.getPairedPrinter()!!.name, Toast.LENGTH_SHORT ).show()
        }
    }
}



