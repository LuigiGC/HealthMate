package com.example.nutrivc

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class AlimentoRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun inserirAlimento(alimento: String, calorias: Double) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ALIMENTO, alimento)
            put(DatabaseHelper.COLUMN_CALORIAS, calorias)
        }
        db.insert(DatabaseHelper.TABLE_NAME, null, values)
    }

    fun inserirAlimentos(alimentos: List<Pair<String, Double>>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            alimentos.forEach { (alimento, calorias) ->
                val values = ContentValues().apply {
                    put(DatabaseHelper.COLUMN_ALIMENTO, alimento)
                    put(DatabaseHelper.COLUMN_CALORIAS, calorias)
                }
                db.insert(DatabaseHelper.TABLE_NAME, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun buscarCalorias(alimento: String): Double? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_CALORIAS),
            "${DatabaseHelper.COLUMN_ALIMENTO} = ?",
            arrayOf(alimento),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val calorias = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CALORIAS))
            cursor.close()
            calorias
        } else {
            cursor.close()
            null
        }
    }

    fun buscarTodosAlimentos(): List<Pair<String, Double>> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_ALIMENTO, DatabaseHelper.COLUMN_CALORIAS),
            null,
            null,
            null,
            null,
            null
        )
        val alimentos = mutableListOf<Pair<String, Double>>()
        while (cursor.moveToNext()) {
            val alimento = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ALIMENTO))
            val calorias = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CALORIAS))
            alimentos.add(alimento to calorias)
        }
        cursor.close()
        return alimentos
    }
}