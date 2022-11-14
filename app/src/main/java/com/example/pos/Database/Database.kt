package com.example.pos.Database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.util.Log

public class Database(context:Context): SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
) {
    companion object {
        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "Kasir"
        private val TABLE_CONTACTS = "Barang2"
        private val KEY_ID = "Kode"
        private val KEY_NAME = "Nama"
        private val KEY_HARGA = "Harga"
        private val KEY_JENIS = "Jenis"
        private val KEY_STOK = "Stok"
        private val TABLE_PENJUALAN = "Penjualan"
        private val KEY_PENJUALAN = "Id_Penjualan"
        private val COL_TOTAL_UNIT = "Total_Unit"
        private val COL_TOTAL_PENJUALAN = "Total_Penjualan"
        private val COL_TANGGAL = "Tanggal"

    }
    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val sql = "CREATE TABLE $TABLE_CONTACTS ($KEY_ID  TEXT PRIMARY KEY, $KEY_NAME TEXT, $KEY_HARGA INTEGER, $KEY_JENIS TEXT, $KEY_STOK INTEGER)"
        val sql2 = "CREATE TABLE $TABLE_PENJUALAN (" +
                "$KEY_PENJUALAN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_ID TEXT , " +
                "$COL_TOTAL_UNIT INTEGER, " +
                "$COL_TOTAL_PENJUALAN INTEGER, " +
                "$COL_TANGGAL DATE ," +
                " FOREIGN KEY ($KEY_ID) REFERENCES $TABLE_CONTACTS ($KEY_ID) ); " +
        Log.d("Data", "onCreate: $sql")
        Log.d("Data", "onCreate: $sql2")
        db?.execSQL(sql)
        db?.execSQL(sql2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_PENJUALAN")
        onCreate(db)
    }

    //method to read data
    fun viewBarang():Cursor{
        val db = this.readableDatabase
        return db.rawQuery("SELECT  * FROM $TABLE_CONTACTS", null)
    }
    //method to read data

    fun viewLaporan():Cursor{
        val db = this.readableDatabase
        return db.rawQuery("SELECT  *FROM $TABLE_PENJUALAN", null)
    }
    //function to read where kode is
    fun RetDatafromKode(kode: String):Cursor {
        val db = this.writableDatabase
        val qeri = "SELECT * FROM $TABLE_CONTACTS WHERE $kode = ?"
        return db.rawQuery(qeri, arrayOf(kode))
    }
    //function to delete
    fun deleteData(kode:String):Int{
        val db = this.writableDatabase
        return db.delete(TABLE_CONTACTS, "Kode=?", arrayOf(kode))
    }

}