package com.example.nutrivc

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                ${COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${COLUMN_ALIMENTO} TEXT NOT NULL,
                ${COLUMN_CALORIAS} REAL NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "nutrivc.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "alimentos"
        const val COLUMN_ID = "id"
        const val COLUMN_ALIMENTO = "alimento"
        const val COLUMN_CALORIAS = "calorias"
    }
}