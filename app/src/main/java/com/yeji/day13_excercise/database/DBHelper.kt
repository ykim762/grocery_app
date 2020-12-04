package com.yeji.day13_excercise.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.yeji.day13_excercise.models.Product

class DBHelper(context: Context) : SQLiteOpenHelper(context, KEY_DB_NAME, null, KEY_DB_VERSION) {

    var db: SQLiteDatabase = this.writableDatabase

    companion object {
        const val KEY_DB_NAME = "mydb"
        const val KEY_DB_VERSION = 4
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_QUANTITY = "quantity"
        const val COLUMN_PRICE = "email"
        const val COLUMN_IMAGE = "image"
        const val COLUMN_MRP = "mrp"
        const val TABLE_NAME = "cart"
    }

    // 맨처음에 디폴트로 돌아감
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {  // create table
        // declared command
        var createTable =
            "create table $TABLE_NAME ($COLUMN_ID char(30), $COLUMN_NAME char(50), $COLUMN_QUANTITY integer, $COLUMN_PRICE integer, $COLUMN_IMAGE char(200), $COLUMN_MRP integer)"

        //execute command
        sqLiteDatabase.execSQL(createTable)
        Log.d("abc", "OnCreate")
    }

    // whenever increase the version number - 절대 줄어들면 안됨
    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, p1: Int, p2: Int) {
        var dropTable = "drop table $TABLE_NAME"  // drop the table 같은 이름 있으면
        sqLiteDatabase!!.execSQL(dropTable)
        onCreate(sqLiteDatabase)
        Log.d("abc", "OnUpgrade")
    }

    fun addProduct(product: Product) {
        if (!isItemInCart(product)) {
            var contentValue = ContentValues()
            contentValue.put(COLUMN_ID, product._id)
            contentValue.put(COLUMN_NAME, product.productName)
            contentValue.put(COLUMN_QUANTITY, product.quantity)
            contentValue.put(COLUMN_PRICE, product.price)
            contentValue.put(COLUMN_IMAGE, product.image)
            contentValue.put(COLUMN_MRP, product.mrp)

            // insert data into table
            db.insert(TABLE_NAME, null, contentValue)
        } else {
            product.quantity++
            updateQuantity(product._id, product.quantity)
        }
    }

    fun isItemInCart(product: Product): Boolean {
        var query = "select * from $TABLE_NAME where $COLUMN_ID = ?"
        var whereArgs = arrayOf(product._id)
        var cursor = db.rawQuery(query, whereArgs)
        var count = cursor.count
        return count != 0
    }

    // update employee set name = '', email = '', where id = 1
    fun updateQuantity(id: String, quantity: Int) {
        var contentValues = ContentValues()
        contentValues.put(COLUMN_QUANTITY, quantity)

        var whereClause = "$COLUMN_ID = ?"  //should not pass directly don't concat
        var whereArgs = arrayOf(id.toString())
        db.update(TABLE_NAME, contentValues, whereClause, whereArgs)
    }

    fun getCartTotalQty():Int {
        var quantity:Int = 0

        var plist = readProduct()
        for(product in plist){
            quantity += product.quantity
        }

        return quantity
    }

    // delete from employee where id = 1
    fun deleteProduct(id: String) {
        var whereClause = "$COLUMN_ID = ?"
        var whereArgs = arrayOf(id)

        db.delete(TABLE_NAME, whereClause, whereArgs)
    }

    fun clearCart() {
        db.delete(TABLE_NAME, null, null)
    }

    fun getProductById(id: String): Product? {
        var product: Product? = null
        var query = "select * from $TABLE_NAME where $COLUMN_ID = ?"
        var whereArg = arrayOf(id)

        var cursor = db.rawQuery(query, whereArg)

        if (cursor.moveToFirst()) { // 처음값만 뽑음
            var id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
            var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            var quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
            var price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
            var image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
            var mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))

            product = Product(
                _id = id,
                price = price,
                productName = name,
                quantity = quantity,
                image = image,
                mrp = mrp
            )
        }
        return product
    }

    fun readProduct(): ArrayList<Product> {
        var list: ArrayList<Product> = ArrayList()

        var columns = arrayOf(
            COLUMN_ID,
            COLUMN_NAME,
            COLUMN_QUANTITY,
            COLUMN_PRICE,
            COLUMN_IMAGE,
            COLUMN_MRP
        )
        // selection - where clause
        var cursor = db.query(TABLE_NAME, columns, null, null, null, null, null)
        if (cursor != null && cursor.moveToFirst()) { //point to the first row
            do {
                // 컬럼 네임 -> 겟 컬럼 인덱스 -> 그 인덱스 넘버 나옴
                var id = cursor.getString(cursor.getColumnIndex(COLUMN_ID))
                var name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                var quantity = cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY))
                var price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE))
                var image = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE))
                var mrp = cursor.getDouble(cursor.getColumnIndex(COLUMN_MRP))

                var product = Product(
                    _id = id,
                    price = price,
                    productName = name,
                    quantity = quantity,
                    image = image,
                    mrp = mrp
                )
                list.add(product)

            } while (cursor.moveToNext())
        }
        return list
    }
}