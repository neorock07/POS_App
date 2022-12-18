package com.example.pos.Activity

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.nfc.Tag
import android.os.Build
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
import com.example.pos.Model.model_barang
import com.example.pos.R
import com.example.pos.RecycleView.Adapter_Struk
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
import kotlin.properties.Delegates

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
    private lateinit var list_kode2:ArrayList<String>
    private lateinit var list_nama2:ArrayList<String>
    private lateinit var list_jenis2:ArrayList<String>
    private lateinit var list_harga2:ArrayList<Int>
    private lateinit var list_jumlah2:ArrayList<Int>
    private lateinit var list_stok:ArrayList<Int>
    var totalHarga : Int = 0
    lateinit var adapter:Adapter_Struk
    private lateinit var bayar:String
    private lateinit var kembali:String
    private var TABLE_CONTACTS = "Barang2"
    private var KEY_ID = "Kode"
    private val KEY_NAME = "Nama"
    private val KEY_HARGA = "Harga"
    private val KEY_JENIS = "Jenis"
    private val KEY_STOK = "Stok"
    private var day:Int = 0
    private var month:Int = 0
    private var year:Int = 0


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
            InsertDataPembelian()
            makeNotif()
            updatedata()
            startActivity(Intent(this@Struk_Activity, MainActivity::class.java))
            finish()
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

    private fun RetrieveData(){
        list_kode = ArrayList<String>()
        list_harga = ArrayList<Int>()
        list_jenis = ArrayList<String>()
        list_nama = ArrayList<String>()
        list_jumlah = ArrayList<Int>()
        list_stok = ArrayList<Int>()

        list_kode2 = intent.getSerializableExtra("key_kode") as ArrayList<String>
        list_nama2 = intent.getSerializableExtra("key_nama") as ArrayList<String>
        list_jenis2 = intent.getSerializableExtra("key_jenis") as ArrayList<String>
        list_harga2 = intent.getSerializableExtra("key_harga") as ArrayList<Int>
        list_jumlah2 = intent.getSerializableExtra("key_jumlah") as ArrayList<Int>
        val stok2 = intent.getSerializableExtra("key_stok") as ArrayList<Int>
        totalHarga = intent.getIntExtra("key_total", 0)
        bayar = intent.getStringExtra("key_bayar")!!
        kembali = intent.getStringExtra("key_return")!!

        for (i in list_jumlah2.indices){
            if (list_jumlah2[i] != 0){
                list_kode.add(list_kode2[i])
                list_harga.add(list_harga2[i])
                list_jenis.add(list_jenis2[i])
                list_nama.add(list_nama2[i])
                list_jumlah.add(list_jumlah2[i])
                list_stok.add(stok2[i])
            }
        }



        if(list_kode.isEmpty()){
            rv.visibility = View.VISIBLE
            btn_print.visibility = View.GONE
        }else{
            adapter = Adapter_Struk(this@Struk_Activity, list_kode, list_nama,list_harga, list_jenis, list_jumlah,list_stok)
            rc.adapter = adapter
        }
    }

    fun InsertDataPembelian(){
        val calender:Calendar = Calendar.getInstance()
        val db:Database = Database(this)
            day = calender.get(Calendar.DAY_OF_MONTH)
            month  = calender.get(Calendar.MONTH) + 1
            year = calender.get(Calendar.YEAR)
            var date:String=""
        if(day <=9){
                date = "$year-$month-0$day"
            }else{
                date =  "$year-$month-$day"
            }

        try{
            for(i in 0..list_kode.size -1){
                db.insertDataPembelian(list_kode[i],list_jumlah[i],list_jumlah[i]*list_harga[i],date)

            }
        }catch (e:Exception){
            Toast.makeText(this, "Error + ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun updatedata(){
            val db: Database=Database(this)
            val dbhelper = db.writableDatabase
            val content : ContentValues = ContentValues()
            for(i in list_stok.indices){
                content.put(KEY_STOK, list_stok[i])
                dbhelper.update(TABLE_CONTACTS,content,"$KEY_ID=?", arrayOf(list_kode[i]))
            }


    }

    fun btnPrint(){
        if(!Printooth.hasPairedPrinter()){
            startActivityForResult(Intent(this@Struk_Activity, ScanningActivity::class.java),ScanningActivity.SCANNING_FOR_PRINTER)
        }else{
            printMsg()
        }
    }

    fun makeNotif(){
        val notif:NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel:NotificationChannel
        val builder:Notification.Builder
        //jika diklik intent pindah ke halaman laporan harian
        val intent:Intent = Intent(this@Struk_Activity, Laporan_Harian::class.java)
        if (month == 1){
            intent.putExtra("Key_Bulan"," Januari")
        }else if (month == 2){
            intent.putExtra("Key_Bulan"," Februari")
        }else if (month == 3){
            intent.putExtra("Key_Bulan"," Maret")
        }else if (month == 4){
            intent.putExtra("Key_Bulan"," April")
        }else if (month == 5){
            intent.putExtra("Key_Bulan"," Mei")
        }else if (month == 6){
            intent.putExtra("Key_Bulan"," Juni")
        }else if (month == 7){
            intent.putExtra("Key_Bulan"," Juli")
        }else if (month == 8){
            intent.putExtra("Key_Bulan"," Agustus")
        }else if (month == 9){
            intent.putExtra("Key_Bulan"," September")
        }else if (month == 10){
            intent.putExtra("Key_Bulan"," Oktober")
        }else if (month == 11){
            intent.putExtra("Key_Bulan"," November")
        }
        else{
            intent.putExtra("Key_Bulan"," Desember")
        }
        intent.putExtra("Key_Tahun",year.toString())

        val pendingIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = NotificationChannel("id","notifikasi transaksi",NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.enableVibration(false)
            notif.createNotificationChannel(channel)

            builder = Notification.Builder(this, "id")
                .setContentText("Transaksi Berhasil! Klik untuk rekap hari ini:)")
                .setSmallIcon(R.drawable.logotiket)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.logo))
                .setContentIntent(pendingIntent)
        }else{
            builder = Notification.Builder(this)
                .setContentText("Transaksi Berhasil! Klik untuk rekap hari ini :)")
                .setSmallIcon(R.drawable.logotiket)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources,R.drawable.logo))
                .setContentIntent(pendingIntent)
        }
        notif.notify(12,builder.build())


    }


    override fun onResume() {
        super.onResume()
        initListener()
        RetrieveData()
    }

    override fun onDestroy() {
        super.onDestroy()
    }


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
        val resource: Resources = resources
        val image : Bitmap= BitmapFactory.decodeResource(resource,R.drawable.logotiket)
        al.add(
            ImagePrintable.Builder(image)
                .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
                .build()
        )
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

        al.add(
            TextPrintable.Builder()
                .setText("===============================")
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
                    .setText("$value\n@$valuePrice x$jml\t Rp ${main.NumberFormat(total.toString())}")
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
        val total = "$totalHarga"
        val myHarga = String.format("Total\t\t Rp.%s", total)
        al.add(
            TextPrintable.Builder()
                .setText(myHarga)
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        val byr = "$bayar"
        val myBayar = String.format("Bayar\t\t Rp.%s", byr)
        al.add(
            TextPrintable.Builder()
                .setText(myBayar)
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        al.add(
            TextPrintable.Builder()
                .setText("Tunai")
                .setCharacterCode(DefaultPrinter.CHARCODE_PC1252)
                .setNewLinesAfter(1)
                .build()
        )
        val kmbl = "$kembali"
        val myKembali = String.format("Kembali\t\t Rp.%s", kmbl)
        al.add(
            TextPrintable.Builder()
                .setText(myKembali)
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
                "Tanggal\t: $day/$month/$year $jam:$menit:$mm\n" + "Total\t: Rp.${main.NumberFormat(totalHarga.toString())}\n\nQRCode made with POS App")
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



