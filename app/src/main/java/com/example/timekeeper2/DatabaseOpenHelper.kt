package com.example.timekeeper2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val DATABASE_NAME = "MyDataBase.db"
val TABLE_NAME = "Times"
val COL_DATE = "date"
val COL_TIME = "time"
val COL_ID = "id"


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context,DATABASE_NAME,null,1)  {

    override fun onCreate(db: SQLiteDatabase?) {

        val createTable = "CREATE TABLE " + TABLE_NAME +" (" +
                COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE + " TEXT,"+
                COL_TIME +" INTEGER)"

        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData (timeDataStructure: timeDataStructure) {
        val db = this.writableDatabase
        var contentValue = ContentValues()
        contentValue.put(COL_DATE,timeDataStructure.date)
        contentValue.put(COL_TIME,timeDataStructure.time)
        db.insert(TABLE_NAME,null,contentValue)
    }

    fun readData() : MutableList<timeDataStructure>{
        var list : MutableList<timeDataStructure> = ArrayList()

        val db = this.readableDatabase
        val query = "Select * from $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var  timeData= timeDataStructure()
                timeData.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                timeData.date = result.getString(result.getColumnIndex(COL_DATE))
                timeData.time = result.getString(result.getColumnIndex(COL_TIME)).toInt()
                list.add(timeData)
            }while (result.moveToNext())
        }

        result.close()
        db.close()
        return list
    }



}
