package com.example.nutrivc

import Meal
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class HistoryActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        databaseHelper = DatabaseHelper(this)

        // Listar todas as tabelas e seus dados no log
        databaseHelper.listarBancoDeDados()

        val selectedDate = intent.getLongExtra("selectedDate", -1)
        if (selectedDate != -1L) {
            val date = java.text.SimpleDateFormat("dd-MM-yyyy", java.util.Locale.getDefault()).format(java.util.Date(selectedDate))
            Log.d("HistoryActivity", "Data selecionada: $date")
            val meals = getMealsForDate(date)
            if (meals.isNotEmpty()) {
                Log.d("HistoryActivity", "Refeições encontradas: ${meals.size}")
                setupRecyclerView(meals)
            } else {
                Log.d("HistoryActivity", "Nenhuma refeição encontrada para a data: $date")
            }
        } else {
            Log.e("HistoryActivity", "Data não selecionada ou inválida")
        }
    }

    private fun getMealsForDate(date: String): List<Meal> {
        val db = databaseHelper.readableDatabase
        val cursor = db.rawQuery("""
            SELECT r.nome, rd.data, SUM(a.calorias * ar.quantidade) as total_calorias
            FROM ${DatabaseHelper.TABLE_REGISTROS_DIARIOS} rd
            JOIN ${DatabaseHelper.TABLE_LOGS_REFEICOES} lr ON rd.${DatabaseHelper.COLUMN_ID} = lr.${DatabaseHelper.COLUMN_REGISTRO_DIARIO_ID}
            JOIN ${DatabaseHelper.TABLE_REFEICOES} r ON lr.${DatabaseHelper.COLUMN_REFEICAO_ID} = r.${DatabaseHelper.COLUMN_ID}
            JOIN ${DatabaseHelper.TABLE_ALIMENTOS_REFEICAO} ar ON lr.${DatabaseHelper.COLUMN_ID} = ar.${DatabaseHelper.COLUMN_LOG_REFEICAO_ID}
            JOIN ${DatabaseHelper.TABLE_ALIMENTOS} a ON ar.${DatabaseHelper.COLUMN_ALIMENTO_ID} = a.${DatabaseHelper.COLUMN_ID}
            WHERE rd.${DatabaseHelper.COLUMN_DATA} = ?
            GROUP BY r.${DatabaseHelper.COLUMN_NOME}, rd.${DatabaseHelper.COLUMN_DATA}
        """, arrayOf(date))

        val meals = mutableListOf<Meal>()
        while (cursor.moveToNext()) {
            val nome = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NOME))
            val horario = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATA))
            val calorias = cursor.getString(cursor.getColumnIndexOrThrow("total_calorias"))
            meals.add(Meal(nome, horario, "$calorias kcal"))
        }
        cursor.close()
        return meals
    }

    private fun setupRecyclerView(meals: List<Meal>) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_meal_history)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MealHistoryAdapter(meals) { meal: Meal ->
            val intent = Intent(this, MealDetailActivity::class.java)
            intent.putExtra("mealName", meal.nome)
            intent.putExtra("mealDate", meal.horario)
            startActivity(intent)
        }
    }

}