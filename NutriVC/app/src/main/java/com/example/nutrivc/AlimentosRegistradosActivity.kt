package com.example.nutrivc

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AlimentosRegistradosActivity : AppCompatActivity() {
    private lateinit var alimentoRepository: AlimentoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alimentos_registrados)

        alimentoRepository = AlimentoRepository(this)

        val alimentos = alimentoRepository.buscarTodosAlimentos()
        val alimentosTextView: TextView = findViewById(R.id.alimentos_text_view)

        val resultados = alimentos.joinToString("\n") { (alimento, calorias) ->
            "$alimento: $calorias calorias"
        }

        alimentosTextView.text = resultados
    }
}