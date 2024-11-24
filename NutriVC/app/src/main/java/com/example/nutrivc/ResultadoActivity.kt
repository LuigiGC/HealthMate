package com.example.nutrivc

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResultadoActivity : AppCompatActivity() {

    private lateinit var alimentoRepository: AlimentoRepository
    private lateinit var resultadosAdapter: ResultadosAdapter
    private val detectedObjects = mutableListOf<BoundingBox>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultado)

        alimentoRepository = AlimentoRepository(this)

        val rvResultados: RecyclerView = findViewById(R.id.rv_resultados)
        val actvAddAlimento: AutoCompleteTextView = findViewById(R.id.actv_add_alimento)
        val spinnerRefeicao: Spinner = findViewById(R.id.spinner_refeicao)
        val btnEnviar: Button = findViewById(R.id.btn_enviar)
        val datePicker: MaterialDatePicker<Long> = MaterialDatePicker.Builder.datePicker().build()

        resultadosAdapter = ResultadosAdapter(detectedObjects)
        rvResultados.layoutManager = LinearLayoutManager(this)
        rvResultados.adapter = resultadosAdapter

        // Receber os alimentos detectados da YoloActivity
        val detectedObjectsList = intent.getParcelableArrayListExtra<BoundingBox>("detectedObjects")
        if (detectedObjectsList != null) {
            detectedObjects.addAll(detectedObjectsList)
            resultadosAdapter.notifyDataSetChanged()
        }

        // Configurar o AutoCompleteTextView com os alimentos disponíveis
        val alimentos = alimentoRepository.buscarTodosAlimentos().map { it.first }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, alimentos)
        actvAddAlimento.setAdapter(adapter)

        actvAddAlimento.setOnItemClickListener { _, _, position, _ ->
            val alimento = adapter.getItem(position) ?: return@setOnItemClickListener
            val calorias = alimentoRepository.buscarCalorias(alimento) ?: return@setOnItemClickListener
            detectedObjects.add(BoundingBox(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, calorias.toFloat(), 0, alimento))
            resultadosAdapter.notifyDataSetChanged()
            actvAddAlimento.text.clear()
        }

        // Configurar o DatePicker para selecionar a data
        val btnDatePicker: Button = findViewById(R.id.btn_date_picker)
        btnDatePicker.setOnClickListener {
            datePicker.show(supportFragmentManager, "datePicker")
        }

        var selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        datePicker.addOnPositiveButtonClickListener { selection ->
            selectedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date(selection))
        }

        // Enviar os dados para o banco de dados
        btnEnviar.setOnClickListener {
            val refeicao = spinnerRefeicao.selectedItem.toString()
            enviarDadosParaBanco(refeicao, selectedDate)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun enviarDadosParaBanco(refeicao: String, data: String) {
        val db = alimentoRepository.dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val registroId = db.insert(DatabaseHelper.TABLE_REGISTROS_DIARIOS, null, ContentValues().apply {
                put(DatabaseHelper.COLUMN_DATA, data)
            })
            val refeicaoId = db.insert(DatabaseHelper.TABLE_REFEICOES, null, ContentValues().apply {
                put(DatabaseHelper.COLUMN_NOME, refeicao)
            })
            val logRefeicaoId = db.insert(DatabaseHelper.TABLE_LOGS_REFEICOES, null, ContentValues().apply {
                put(DatabaseHelper.COLUMN_REGISTRO_DIARIO_ID, registroId)
                put(DatabaseHelper.COLUMN_REFEICAO_ID, refeicaoId)
            })
            detectedObjects.forEach { box ->
                val alimentoId = db.query(DatabaseHelper.TABLE_ALIMENTOS, arrayOf(DatabaseHelper.COLUMN_ID), "${DatabaseHelper.COLUMN_NOME} = ?", arrayOf(box.clsName), null, null, null).use {
                    if (it.moveToFirst()) it.getLong(it.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID)) else -1
                }
                db.insert(DatabaseHelper.TABLE_ALIMENTOS_REFEICAO, null, ContentValues().apply {
                    put(DatabaseHelper.COLUMN_LOG_REFEICAO_ID, logRefeicaoId)
                    put(DatabaseHelper.COLUMN_ALIMENTO_ID, alimentoId)
                    put(DatabaseHelper.COLUMN_QUANTIDADE, 100) // Exemplo de quantidade fixa
                })
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }
}