package com.example.nutrivc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val recyclerView = findViewById<RecyclerView>(R.id.rv_meal_history)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sampleMeals = listOf(
            Meal("Café da Manhã", "Hoje, 08:00", "250 kcal"),
            Meal("Almoço", "Ontem, 13:00", "600 kcal"),
            Meal("Jantar", "Ontem, 19:30", "450 kcal")
        )

        // Configura o RecyclerView com o adaptador
        recyclerView.adapter = MealHistoryAdapter(sampleMeals)
    }
}

