package com.example.nutrivc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ResultadosAdapter(private val resultados: List<BoundingBox>) : RecyclerView.Adapter<ResultadosAdapter.ResultadoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultadoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        return ResultadoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResultadoViewHolder, position: Int) {
        val resultado = resultados[position]
        holder.nomeTextView.text = resultado.clsName
        holder.caloriasTextView.text = "${resultado.cnf} kcal"
    }

    override fun getItemCount(): Int = resultados.size

    class ResultadoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeTextView: TextView = itemView.findViewById(R.id.tv_meal_name)
        val caloriasTextView: TextView = itemView.findViewById(R.id.tv_meal_calories)
    }
}