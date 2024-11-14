package com.example.nutrivc

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.File

class ScanActivity : AppCompatActivity() {

    private lateinit var photoUri: Uri

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            openImageActivity(it)
        }
    }

    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            openImageActivity(photoUri)
        } else {
            Toast.makeText(this, "Falha ao capturar a imagem", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scan)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        showImageSourceDialog()
        val imageView: ImageView = findViewById(R.id.imageView)
        val btnTakeAnother: Button = findViewById(R.id.btn_take_another)
        val btnSendAnalysis: Button = findViewById(R.id.btn_send_analysis)

        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        imageView.setImageURI(imageUri)

        btnTakeAnother.setOnClickListener {
            recreate()
        }

        btnSendAnalysis.setOnClickListener {
            if (imageUri != null) {
                val intent = Intent(this, YoloActivity::class.java).apply {
                    putExtra("imageUri", imageUri.toString())
                }
                startActivity(intent)
                finish()

            } else {
                Toast.makeText(this, "Nenhuma foto carregada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Galeria", "Câmera")
        AlertDialog.Builder(this)
            .setTitle("Escolha uma opção")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> pickImage.launch("image/*")
                    1 -> {
                        val photoFile = createImageFile()
                        photoUri = FileProvider.getUriForFile(this, "com.example.nutrivc.provider", photoFile)
                        takePicture.launch(photoUri)
                    }
                }
            }
            .show()
    }

    private fun createImageFile(): File {
        val storageDir = getExternalFilesDir(null)
        return File.createTempFile("photo_", ".jpg", storageDir)
    }

    private fun openImageActivity(imageUri: Uri) {
        val intent = Intent(this, ImageActivity::class.java).apply {
            putExtra("imageUri", imageUri.toString())
        }
        startActivity(intent)
    }
}