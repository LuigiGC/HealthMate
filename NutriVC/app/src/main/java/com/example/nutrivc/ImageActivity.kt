package com.example.nutrivc

import YoloActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val imageView: ImageView = findViewById(R.id.imageView)
        val btnTakeAnother: Button = findViewById(R.id.btn_take_another)
        val btnSendAnalysis: Button = findViewById(R.id.btn_send_analysis)

        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        imageView.setImageURI(imageUri)

        btnTakeAnother.setOnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        }

        btnSendAnalysis.setOnClickListener {
            if (imageUri != null) {
                val intent = Intent(this, YoloActivity::class.java).apply {
                    putExtra("imageUri", imageUri.toString())
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Nenhuma foto carregada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}