package com.example.nutrivc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodDetailAdapter(private val foods: List<Food>) : RecyclerView.Adapter<FoodDetailAdapter.FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        holder.nomeTextView.text = food.nome
        holder.quantidadeTextView.text = "${food.quantidade}g"
        holder.caloriasTextView.text = "${food.calorias} kcal"
    }

    override fun getItemCount(): Int = foods.size

    class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.tv_food_name)
        val quantidadeTextView: TextView = itemView.findViewById(R.id.tv_food_quantity)
        val caloriasTextView: TextView = itemView.findViewById(R.id.tv_food_calories)
    }
}