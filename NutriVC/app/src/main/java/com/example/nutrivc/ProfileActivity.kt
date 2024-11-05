package com.example.nutrivc

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val nameEditText = findViewById<EditText>(R.id.et_name)
        val weightEditText = findViewById<EditText>(R.id.et_weight)
        val saveButton = findViewById<Button>(R.id.btn_save_profile)

        // Exemplo de dados fictícios (esses valores podem ser recuperados de um possivel DB)
        nameEditText.setText("Bonequinho")
        weightEditText.setText("75 kg")

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val weight = weightEditText.text.toString()

            // Possivel integração com DB (verificar facilidade)
        }
    }
}
