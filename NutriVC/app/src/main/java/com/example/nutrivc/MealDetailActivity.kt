package com.example.nutrivc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MealDetailActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_detail)

        databaseHelper = DatabaseHelper(this)

        val mealName = intent.getStringExtra("mealName")
        val mealDate = intent.getStringExtra("mealDate")

        if (mealName != null && mealDate != null) {
            val foods = getFoodsForMeal(mealName, mealDate)
            setupRecyclerView(foods)
        }
    }

    private fun getFoodsForMeal(mealName: String, mealDate: String): List<Food> {
        val db = databaseHelper.readableDatabase
        val cursor = db.rawQuery("""
            SELECT a.nome, ar.quantidade, a.calorias
            FROM registros_diarios rd
            JOIN logs_refeicoes lr ON rd.id = lr.registro_diario_id
            JOIN refeicoes r ON lr.refeicao_id = r.id
            JOIN alimentos_refeicao ar ON lr.id = ar.log_refeicao_id
            JOIN alimentos a ON ar.alimento_id = a.id
            WHERE r.nome = ? AND rd.data = ?
        """, arrayOf(mealName, mealDate))

        val foods = mutableListOf<Food>()
        while (cursor.moveToNext()) {
            val nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"))
            val quantidade = cursor.getInt(cursor.getColumnIndexOrThrow("quantidade"))
            val calorias = cursor.getInt(cursor.getColumnIndexOrThrow("calorias"))
            foods.add(Food(nome, quantidade, calorias))
        }
        cursor.close()
        return foods
    }

    private fun setupRecyclerView(foods: List<Food>) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_food_detail)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = FoodDetailAdapter(foods)
    }
}