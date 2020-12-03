package com.yeji.day13_excercise.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.yeji.day13_excercise.models.Address
import com.yeji.day13_excercise.models.Product

class DBAddressHelper(context: Context) :
    SQLiteOpenHelper(context, KEY_DB_NAME, null, KEY_DB_VERSION) {

    var db: SQLiteDatabase = this.writableDatabase

    companion object {
        const val KEY_DB_NAME = "mydb_address"
        const val KEY_DB_VERSION = 2
        const val COLUMN_ID = "id"
        const val COLUMN_CITY = "city"
        const val COLUMN_HOUSENUM = "housenum"
        const val COLUMN_ZIPCODE = "zipcode"
        const val COLUMN_STNAME = "streetname"
        const val COLUMN_TYPE = "type"
        const val COLUMN_USERID = "userid"
        const val TABLE_NAME = "address"
    }

    override fun onCreate(sqlListeDatabase: SQLiteDatabase) {
        var createTable =
            "create table $TABLE_NAME ($COLUMN_ID char(30), $COLUMN_CITY char(30), $COLUMN_HOUSENUM char(10), $COLUMN_ZIPCODE char(10), $COLUMN_STNAME char(30), $COLUMN_TYPE char(20), $COLUMN_USERID char(30))"
        sqlListeDatabase.execSQL(createTable)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, p1: Int, p2: Int) {
        var dropTable = "drop table $TABLE_NAME"  // drop the table 같은 이름 있으면
        sqLiteDatabase!!.execSQL(dropTable)
        onCreate(sqLiteDatabase)
        Log.d("abc", "OnUpgrade")
    }

    fun addAddress(address: Address) {
        var contentValue = ContentValues()
        contentValue.put(COLUMN_ID, address._id)
        contentValue.put(COLUMN_CITY, address.city)
        contentValue.put(COLUMN_HOUSENUM, address.houseNo)
        contentValue.put(COLUMN_ZIPCODE, address.pincode)
        contentValue.put(COLUMN_STNAME, address.streetName)
        contentValue.put(COLUMN_TYPE, address.type)
        contentValue.put(COLUMN_USERID, address.userId)

        // insert data into table
        db.insert(TABLE_NAME, null, contentValue)
    }

    fun deleteAddress(id: String) {
        var whereClause = "$COLUMN_ID = ?"
        var whereArgs = arrayOf(id)

        db.delete(TABLE_NAME, whereClause, whereArgs)
    }

    fun readAddress(): ArrayList<Address> {
        var list: ArrayList<Address> = ArrayList()

        var columns = arrayOf(
            COLUMN_ID,
            COLUMN_CITY,
            COLUMN_HOUSENUM,
            COLUMN_ZIPCODE,
            COLUMN_STNAME,
            COLUMN_TYPE,
            COLUMN_USERID
        )
        // selection - where clause
        var cursor = db.query(TABLE_NAME, columns, null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) { //point to the first row
            do {
                var id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                var city = cursor.getString(cursor.getColumnIndex(COLUMN_CITY))
                var housenum = cursor.getString(cursor.getColumnIndex(COLUMN_HOUSENUM))
                var zipcode = cursor.getString(cursor.getColumnIndex(COLUMN_ZIPCODE))
                var stname = cursor.getString(cursor.getColumnIndex(COLUMN_STNAME))
                var type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE))
                var userid = cursor.getString(cursor.getColumnIndex(COLUMN_USERID))

                var address = Address(id, city, housenum, zipcode, stname, type, userid)
                list.add(address)

            } while (cursor.moveToNext())
        }
        return list
    }

}