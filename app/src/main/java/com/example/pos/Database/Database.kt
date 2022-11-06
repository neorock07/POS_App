package com.example.pos.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.util.Log

public class database(context:Context): SQLiteOpenHelper(context,
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
    }
    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val sql = "CREATE TABLE $TABLE_CONTACTS ($KEY_ID  TEXT PRIMARY KEY, $KEY_NAME TEXT, $KEY_HARGA INTEGER, $KEY_JENIS TEXT, $KEY_STOK INTEGER)"
        Log.d("Data", "onCreate: $sql");
        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }


    //method to read data
    fun viewBarang():Cursor{
        val db = this.readableDatabase
        return db.rawQuery("SELECT  * FROM $TABLE_CONTACTS", null)
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

