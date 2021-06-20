package com.muhammetguler.mytakingnoteapp

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val dbname = "myNotes"
val dbtable = "notes"
val col_id = "id"
val col_title = "title"
val col_descrp = "description"
val db_version = 1
var resultList = ArrayList<MyNotes>()

class Database:SQLiteOpenHelper {
    var context:Context?=null
    constructor(
        context: Context?,
    ) : super(context, dbname, null, db_version)
    {
        this.context=context
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Tablo oluşturma
          db!!.execSQL("CREATE TABLE " + dbtable + " (" + col_id + " INTEGER PRIMARY KEY," + col_title + " TEXT, " + col_descrp + " TEXT);")

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun store(content:ContentValues):Long{
        val db = this.writableDatabase
        val result = db.insert(dbtable, null, content)
        db.close()
        return result
    }

    fun query(): ArrayList<MyNotes>{
        val db = this.readableDatabase
        val query = "SELECT * FROM " + dbtable
        var cursor = db.rawQuery(query, null)
        if(cursor.moveToFirst())
        {
            do {

                var id = cursor.getInt(cursor.getColumnIndexOrThrow(col_id))
                var name = cursor.getString(cursor.getColumnIndexOrThrow(col_title))
                var des = cursor.getString(cursor.getColumnIndexOrThrow(col_descrp))
                resultList.add(MyNotes(id,name,des))

            } while (cursor.moveToNext())

        }
        return resultList
    }
    fun checkSize():Int{
        var counter = 0
        val database = this.readableDatabase
        val queryPRMS = "SELECT * FROM " + dbtable
        val cursor = database!!.rawQuery(queryPRMS, null)
        if(cursor.moveToFirst())
            do {
                counter++
            }while(cursor.moveToNext())
        return counter
    }

    fun deleteNote (id:Int):Int{
        val database = this.writableDatabase
        val count = database!!.delete(dbtable, "ID=?", arrayOf(id.toString()))
        database.close()
        return count
    }

    fun updateNote (id:Int, title:String, descrp:String):Int{
        val database = this.writableDatabase
        var values = ContentValues()
        values.put(col_title, title)
        values.put(col_descrp, descrp)
        val count = database!!.update(dbtable, values, "ID=?", arrayOf(id.toString()))
        database.close()
        return count
    }
}