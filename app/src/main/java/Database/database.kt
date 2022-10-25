package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteException
import android.util.Log

public class database(context:Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
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


    //method to insert data
    fun addBarang(emp: EmpModelClass):Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.kodeBarang)
        contentValues.put(KEY_NAME, emp.namaBarang)
        contentValues.put(KEY_STOK,emp.stok)// EmpModelClass Name
        contentValues.put(KEY_HARGA,emp.hargaBarang)
        contentValues.put(KEY_JENIS,emp.jenis)// EmpModelClass Phone
        // Inserting Row
        val success = db.insert(TABLE_CONTACTS, null, contentValues)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to read data
    fun viewBarang():Cursor{

        val db = this.readableDatabase
        return db.rawQuery("SELECT  * FROM $TABLE_CONTACTS", null)
    }
    //method to update data
    fun updateBarang(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.kodeBarang)
        contentValues.put(KEY_NAME, emp.namaBarang)
        contentValues.put(KEY_STOK,emp.stok)// EmpModelClass Name
        contentValues.put(KEY_HARGA,emp.hargaBarang)
        contentValues.put(KEY_JENIS,emp.jenis)
        // EmpModelClass Email

        // Updating Row
        val success = db.update(TABLE_CONTACTS, contentValues,"Kode="+emp.kodeBarang,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
    //method to delete data
    fun deleteBarang(emp: EmpModelClass):Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, emp.kodeBarang) // EmpModelClass UserId
        // Deleting Row
        val success = db.delete(TABLE_CONTACTS,"Kode="+emp.kodeBarang,null)
        //2nd argument is String containing nullColumnHack
        db.close() // Closing database connection
        return success
    }
}

