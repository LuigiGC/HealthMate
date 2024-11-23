package com.example.nutrivc

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultadoActivity : AppCompatActivity() {

    private lateinit var alimentoRepository: AlimentoRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        alimentoRepository = AlimentoRepository(this)

        val detectedObjects: List<BoundingBox> = intent.getParcelableArrayListExtra("detectedObjects") ?: listOf()
        val resultadoTextView: TextView = findViewById(R.id.resultado_text_view)

        val alimentos = listOf(
            "apple" to 52.0,
            "Bananna" to 96.0,
            "Orange" to 43.0

        )
        alimentoRepository.inserirAlimentos(alimentos)


        val resultados = detectedObjects.joinToString("\n") { box ->
            val calorias = alimentoRepository.buscarCalorias(box.clsName)
            "${box.clsName}: ${calorias ?: "Calorias não encontradas"}"
        }

        resultadoTextView.text = resultados
    }
}