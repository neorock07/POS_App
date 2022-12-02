package com.example.pos.Database


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor
import android.util.Log

public class Database(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
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
        private val TABLE_USER = "User"
        private val KEY_USER = "Id_User"
        private val COL_USERNAME = "Username"
        private val COL_PASSWORD = "Password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //creating table with fields
        val sql =
            "CREATE TABLE $TABLE_CONTACTS (" +
                    "$KEY_ID  TEXT PRIMARY KEY, " +
                    "$KEY_NAME TEXT, " +
                    "$KEY_HARGA INTEGER, " +
                    "$KEY_JENIS TEXT, " +
                    "$KEY_STOK INTEGER )"

        val sql2 = "CREATE TABLE $TABLE_PENJUALAN (" +
                "$KEY_PENJUALAN INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$KEY_ID TEXT , " +
                "$COL_TOTAL_UNIT INTEGER, " +
                "$COL_TOTAL_PENJUALAN INTEGER, " +
                "$COL_TANGGAL DATE ," +
                " FOREIGN KEY ($KEY_ID) REFERENCES $TABLE_CONTACTS ($KEY_ID) )"

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
    fun viewBarang(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT  * FROM $TABLE_CONTACTS", null)
    }
    //method to load data grouping by year
    fun readIncomePerYear():Cursor{
        val db = this.readableDatabase
        //SELECT strftime('%Y',TANGGAL) AS TAHUN,SUM(JML_BRG) FROM TB_PEMBELIAN
        //GROUP BY strftime('%Y',TANGGAL) ORDER BY strftime('%Y',TANGGAL) DESC
        return db.rawQuery("SELECT strftime('%Y',$COL_TANGGAL) AS TAHUN, SUM($COL_TOTAL_PENJUALAN) AS INCOME FROM $TABLE_PENJUALAN" +
                " GROUP BY strftime('%Y',$COL_TANGGAL) ORDER BY strftime('%Y',$COL_TANGGAL) DESC",null)
    }
    //method to insert data Pembelian
    fun insertDataPembelian(id:String,unit:Int, total:Int,tanggal:String){
        val db = this.writableDatabase
        var contentValues:ContentValues = ContentValues()
        contentValues.put(KEY_ID,id)
        contentValues.put(COL_TOTAL_UNIT, unit)
        contentValues.put(COL_TOTAL_PENJUALAN, total)
        contentValues.put(COL_TANGGAL, tanggal)
        db.insert(TABLE_PENJUALAN, null, contentValues)
    }
    //method to load data per month
    fun readIncomePerMonth(tahun:String):Cursor{
        val db = this.readableDatabase
        return db.rawQuery("SELECT CASE " +
                "WHEN strftime('%m',$COL_TANGGAL) = '01' THEN 'Januari' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '02' THEN 'Februari' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '03' THEN 'Maret' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '04' THEN 'April' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '05' THEN 'Mei' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '06' THEN 'Juni' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '07' THEN 'Juli' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '08' THEN 'Agustus' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '09' THEN 'September' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '10' THEN 'Oktober' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '11' THEN 'November' " +
                "WHEN strftime('%m',$COL_TANGGAL) = '12' THEN 'Desember' " +
                "END AS BULAN," +
                " SUM($COL_TOTAL_PENJUALAN) AS INCOME FROM $TABLE_PENJUALAN GROUP BY strftime('%m',$COL_TANGGAL) HAVING strftime('%Y',$COL_TANGGAL) = '$tahun'",null)
                   }
    fun readPerTanggal(bulan: String, tahun: String): Cursor{
        val db = this.readableDatabase
        return db.rawQuery("SELECT distinct strftime('%d', Penjualan.tanggal) AS TANGGAL1 FROM Barang2 " +
                "LEFT JOIN Penjualan " +
                "ON Barang2.kode = Penjualan.kode " +
                "WHERE strftime('%Y %m',Penjualan.tanggal) = '$tahun $bulan'", null)
    }
fun readPerDetail(tanggal : String, tahun: String, bulan: String):Cursor{
    var ymd = "$tahun-$bulan-$tanggal"
    val db = this.readableDatabase
    return db.rawQuery("SELECT Barang2.Kode, Nama, Harga, Jenis, Penjualan.Total_Unit, Penjualan.Tanggal AS TANGGAL1 FROM Barang2 " +
            "                LEFT JOIN Penjualan " +
            "                ON Barang2.Kode = Penjualan.Kode " +
            "                WHERE Penjualan.Tanggal = '$ymd'", null)
}
    fun viewUser(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT  * FROM $TABLE_USER", null)
    }

    fun viewLaporan(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT  *FROM $TABLE_PENJUALAN", null)
    }

    //function to delete
    fun deleteData(kode: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_CONTACTS, "Kode=?", arrayOf(kode))
    }

}