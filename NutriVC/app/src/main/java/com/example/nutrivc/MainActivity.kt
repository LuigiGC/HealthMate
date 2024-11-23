package com.example.nutrivc

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btEscanear: Button = findViewById(R.id.bt_Escanear)
        val tvHistory: Button = findViewById(R.id.tv_history)
        val ibtProfile: ImageButton = findViewById(R.id.ibt_profile)
        val bt_alimentos_registrados: Button = findViewById(R.id.bt_alimentos_registrados)

        btEscanear.setOnClickListener {
            // Adicione a funcionalidade desejada aqui
            // Por exemplo, iniciar uma nova Activity
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }

        tvHistory.setOnClickListener {
            // Adicione a funcionalidade desejada aqui
            // Por exemplo, iniciar uma nova Activity
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
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