package com.example.nutrivc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MealHistoryAdapter(private val mealList: List<Meal>) : RecyclerView.Adapter<MealHistoryAdapter.MealViewHolder>() {

    inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealName: TextView = view.findViewById(R.id.tv_meal_name)
        val mealTime: TextView = view.findViewById(R.id.tv_meal_time)
        val mealCalories: TextView = view.findViewById(R.id.tv_meal_calories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = mealList[position]
        holder.mealName.text = meal.name
        holder.mealTime.text = meal.time
        holder.mealCalories.text = meal.calories
    }

    override fun getItemCount() = mealList.size
}
