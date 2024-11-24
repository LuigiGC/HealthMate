package com.example.nutrivc

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        databaseHelper.popularBancoDeDados(this)

        databaseHelper.listarBancoDeDados()
        databaseHelper.executarConsulta()
        val alimentos = databaseHelper.buscarAlimentosPorData("21-11-2024")
        for (alimento in alimentos) {
            Log.d("DatabaseHelper3", "Alimento: ${alimento.first}, Quantidade: ${alimento.second}g")
        }

        val btEscanear: Button = findViewById(R.id.bt_Escanear)
        val tvHistory: Button = findViewById(R.id.tv_history)
        val ibtProfile: ImageButton = findViewById(R.id.ibt_profile)
        val bt_alimentos_registrados: Button = findViewById(R.id.bt_alimentos_registrados)

        btEscanear.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }

        tvHistory.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                val intent = Intent(this, HistoryActivity::class.java)
                intent.putExtra("selectedDate", selection)
                startActivity(intent)
            }
            datePicker.show(supportFragmentManager, "datePicker")
        }

        ibtProfile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        bt_alimentos_registrados.setOnClickListener {
            val intent = Intent(this, AlimentosRegistradosActivity::class.java)
            startActivity(intent)
        }
    }
}