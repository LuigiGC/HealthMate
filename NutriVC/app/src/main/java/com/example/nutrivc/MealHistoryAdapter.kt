package com.example.nutrivc

import Meal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MealHistoryAdapter(
    private val meals: List<Meal>,
    private val onClick: (Meal) -> Unit
) : RecyclerView.Adapter<MealHistoryAdapter.MealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.nomeTextView.text = meal.nome
        holder.horarioTextView.text = meal.horario
        holder.caloriasTextView.text = meal.calorias
        holder.itemView.setOnClickListener { onClick(meal) }
    }

    override fun getItemCount(): Int = meals.size

    class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.tv_meal_name)
        val horarioTextView: TextView = itemView.findViewById(R.id.tv_meal_time)
        val caloriasTextView: TextView = itemView.findViewById(R.id.tv_meal_calories)
    }
}