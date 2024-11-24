package com.example.nutrivc

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class AlimentoRepository(context: Context) {

    val dbHelper = DatabaseHelper(context)

    fun inserirAlimento(alimento: String, calorias: Double) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NOME, alimento)
            put(DatabaseHelper.COLUMN_CALORIAS, calorias)
        }
        db.insert(DatabaseHelper.TABLE_ALIMENTOS, null, values)
    }

    fun inserirAlimentos(alimentos: List<Pair<String, Double>>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            alimentos.forEach { (alimento, calorias) ->
                val values = ContentValues().apply {
                    put(DatabaseHelper.COLUMN_NOME, alimento)
                    put(DatabaseHelper.COLUMN_CALORIAS, calorias)
                }
                db.insert(DatabaseHelper.TABLE_ALIMENTOS, null, values)
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun buscarCalorias(alimento: String): Double? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_ALIMENTOS,
            arrayOf(DatabaseHelper.COLUMN_CALORIAS),
            "${DatabaseHelper.COLUMN_NOME} = ?",
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
            DatabaseHelper.TABLE_ALIMENTOS,
            arrayOf(DatabaseHelper.COLUMN_NOME, DatabaseHelper.COLUMN_CALORIAS),
            null,
            null,
            null,
            null,
            null
        )
        val alimentos = mutableListOf<Pair<String, Double>>()
        while (cursor.moveToNext()) {
            val alimento = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOME))
            val calorias = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CALORIAS))
            alimentos.add(alimento to calorias)
        }
        cursor.close()
        return alimentos
    }
    fun carregarDados(context: Context) {
        val alimentoRepository = AlimentoRepository(context)

        val alimentos = listOf(
            "apple" to 52.0,
            "banana" to 96.0,
            "orange" to 43.0,
            "mango" to 60.0,
            "pineapple" to 50.0,
            "strawberry" to 32.0,
            "watermelon" to 30.0,
            "melon" to 34.0,
            "peach" to 39.0,
            "pear" to 57.0,
            "kiwi" to 61.0,
            "grape" to 69.0,
            "cherry" to 50.0,
            "plum" to 46.0,
            "papaya" to 43.0,
            "lemon" to 29.0,
            "avocado" to 160.0,
            "coconut" to 354.0,
            "raspberry" to 52.0,
            "blackberry" to 43.0
        )

        alimentoRepository.inserirAlimentos(alimentos)
    }
}