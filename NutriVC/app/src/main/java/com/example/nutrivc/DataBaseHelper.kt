package com.example.nutrivc

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createRefeicoesTable = """
            CREATE TABLE $TABLE_REFEICOES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOME TEXT NOT NULL
            )
        """.trimIndent()

        val createAlimentosTable = """
            CREATE TABLE $TABLE_ALIMENTOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NOME TEXT NOT NULL,
                $COLUMN_CALORIAS INTEGER NOT NULL
            )
        """.trimIndent()

        val createRegistrosDiariosTable = """
            CREATE TABLE $TABLE_REGISTROS_DIARIOS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_DATA TEXT NOT NULL
            )
        """.trimIndent()

        val createLogsRefeicoesTable = """
            CREATE TABLE $TABLE_LOGS_REFEICOES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_REGISTRO_DIARIO_ID INTEGER NOT NULL,
                $COLUMN_REFEICAO_ID INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_REGISTRO_DIARIO_ID) REFERENCES $TABLE_REGISTROS_DIARIOS($COLUMN_ID),
                FOREIGN KEY ($COLUMN_REFEICAO_ID) REFERENCES $TABLE_REFEICOES($COLUMN_ID)
            )
        """.trimIndent()

        val createAlimentosRefeicaoTable = """
            CREATE TABLE $TABLE_ALIMENTOS_REFEICAO (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_LOG_REFEICAO_ID INTEGER NOT NULL,
                $COLUMN_ALIMENTO_ID INTEGER NOT NULL,
                $COLUMN_QUANTIDADE INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_LOG_REFEICAO_ID) REFERENCES $TABLE_LOGS_REFEICOES($COLUMN_ID),
                FOREIGN KEY ($COLUMN_ALIMENTO_ID) REFERENCES $TABLE_ALIMENTOS($COLUMN_ID)
            )
        """.trimIndent()

        db.execSQL(createRefeicoesTable)
        db.execSQL(createAlimentosTable)
        db.execSQL(createRegistrosDiariosTable)
        db.execSQL(createLogsRefeicoesTable)
        db.execSQL(createAlimentosRefeicaoTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALIMENTOS_REFEICAO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOGS_REFEICOES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REGISTROS_DIARIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ALIMENTOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_REFEICOES")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "HealthMate.db"
        const val DATABASE_VERSION = 1

        const val TABLE_REFEICOES = "refeicoes"
        const val TABLE_ALIMENTOS = "alimentos"
        const val TABLE_REGISTROS_DIARIOS = "registros_diarios"
        const val TABLE_LOGS_REFEICOES = "logs_refeicoes"
        const val TABLE_ALIMENTOS_REFEICAO = "alimentos_refeicao"

        const val COLUMN_ID = "id"
        const val COLUMN_NOME = "nome"
        const val COLUMN_CALORIAS = "calorias"
        const val COLUMN_DATA = "data"
        const val COLUMN_REGISTRO_DIARIO_ID = "registro_diario_id"
        const val COLUMN_REFEICAO_ID = "refeicao_id"
        const val COLUMN_LOG_REFEICAO_ID = "log_refeicao_id"
        const val COLUMN_ALIMENTO_ID = "alimento_id"
        const val COLUMN_QUANTIDADE = "quantidade"
    }
    fun listarBancoDeDados() {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)
        if (cursor.moveToFirst()) {
            do {
                val tableName = cursor.getString(0)
                Log.d("DatabaseHelper", "Tabela: $tableName")
                listarDadosTabela(tableName)
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    private fun listarDadosTabela(tableName: String) {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $tableName", null)
        if (cursor.moveToFirst()) {
            do {
                val row = StringBuilder()
                for (i in 0 until cursor.columnCount) {
                    row.append("${cursor.getColumnName(i)}: ${cursor.getString(i)}; ")
                }
                Log.d("DatabaseHelper", row.toString())
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
    fun executarConsulta() {
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM ${DatabaseHelper.TABLE_REGISTROS_DIARIOS} WHERE TRIM(${DatabaseHelper.COLUMN_DATA}) = '23-11-2024'", null)
        if (cursor.moveToFirst()) {
            do {
                val row = StringBuilder()
                for (i in 0 until cursor.columnCount) {
                    row.append("${cursor.getColumnName(i)}: ${cursor.getString(i)}; ")
                }
                Log.d("DatabaseHelper2", row.toString())
            } while (cursor.moveToNext())
        } else {
            Log.d("DatabaseHelper", "Nenhum resultado encontrado.")
        }
        cursor.close()
    }
    fun buscarAlimentosPorData(data: String): List<Pair<String, Int>> {
        val db = readableDatabase
        val cursor = db.rawQuery("""
        SELECT a.${DatabaseHelper.COLUMN_NOME}, ar.${DatabaseHelper.COLUMN_QUANTIDADE}
        FROM ${DatabaseHelper.TABLE_REGISTROS_DIARIOS} rd
        JOIN ${DatabaseHelper.TABLE_LOGS_REFEICOES} lr ON rd.${DatabaseHelper.COLUMN_ID} = lr.${DatabaseHelper.COLUMN_REGISTRO_DIARIO_ID}
        JOIN ${DatabaseHelper.TABLE_REFEICOES} r ON lr.${DatabaseHelper.COLUMN_REFEICAO_ID} = r.${DatabaseHelper.COLUMN_ID}
        JOIN ${DatabaseHelper.TABLE_ALIMENTOS_REFEICAO} ar ON lr.${DatabaseHelper.COLUMN_ID} = ar.${DatabaseHelper.COLUMN_LOG_REFEICAO_ID}
        JOIN ${DatabaseHelper.TABLE_ALIMENTOS} a ON ar.${DatabaseHelper.COLUMN_ALIMENTO_ID} = a.${DatabaseHelper.COLUMN_ID}
        WHERE rd.${DatabaseHelper.COLUMN_DATA} = ?
    """, arrayOf(data))

        val alimentos = mutableListOf<Pair<String, Int>>()
        while (cursor.moveToNext()) {
            val nome = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOME))
            val quantidade = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_QUANTIDADE))
            alimentos.add(nome to quantidade)
        }
        cursor.close()
        return alimentos
    }

    fun popularBancoDeDados(context: Context) {
        val dbHelper = DatabaseHelper(context)
        val db: SQLiteDatabase = dbHelper.writableDatabase

        db.beginTransaction()
        try {
            // Inserir registro diário
            val registroId = db.insert(DatabaseHelper.TABLE_REGISTROS_DIARIOS, null, ContentValues().apply {
                put(DatabaseHelper.COLUMN_DATA, "21-11-2024")
            })

            // Inserir refeição
            val refeicaoId = db.insert(DatabaseHelper.TABLE_REFEICOES, null, ContentValues().apply {
                put(DatabaseHelper.COLUMN_NOME, "Almoço")
            })

            // Inserir log de refeição
            val logRefeicaoId = db.insert(DatabaseHelper.TABLE_LOGS_REFEICOES, null, ContentValues().apply {
                put(DatabaseHelper.COLUMN_REGISTRO_DIARIO_ID, registroId)
                put(DatabaseHelper.COLUMN_REFEICAO_ID, refeicaoId)
            })

            // Buscar ID do alimento 'apple'
            val cursor = db.query(DatabaseHelper.TABLE_ALIMENTOS, arrayOf(DatabaseHelper.COLUMN_ID), "${DatabaseHelper.COLUMN_NOME} = ?", arrayOf("apple"), null, null, null)
            val alimentoId = if (cursor.moveToFirst()) cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)) else -1
            cursor.close()

            // Inserir alimento na refeição
            db.insert(DatabaseHelper.TABLE_ALIMENTOS_REFEICAO, null, ContentValues().apply {
                put(DatabaseHelper.COLUMN_LOG_REFEICAO_ID, logRefeicaoId)
                put(DatabaseHelper.COLUMN_ALIMENTO_ID, alimentoId)
                put(DatabaseHelper.COLUMN_QUANTIDADE, 100) // Exemplo de quantidade fixa
            })

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}