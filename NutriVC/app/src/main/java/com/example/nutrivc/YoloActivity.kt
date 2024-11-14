package com.example.nutrivc

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nutrivc.R
import com.example.nutrivc.ml.Yolo11nFloat32
import org.tensorflow.lite.support.image.TensorImage
import java.io.FileNotFoundException
import java.io.InputStream

class YoloActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yolo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtém a URI da imagem a partir da Intent
        val imageView: ImageView = findViewById(R.id.imageView)
        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        imageView.setImageURI(imageUri)

        // Processa a imagem se a URI não for nula
        imageUri?.let {
            processImage(it)
        }
    }

    private fun processImage(imageUri: Uri) {
        try {
            // Abre o InputStream da imagem usando a URI
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Inicializa o modelo YOLO e cria um TensorImage a partir do Bitmap
            val model = Yolo11nFloat32.newInstance(this)
            val image = TensorImage.fromBitmap(bitmap)

            // Executa a inferência no modelo e obtém o resultado
            val outputs = model.process(image)
            val output = outputs.outputAsCategoryList

            // Libera os recursos do modelo
            model.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}
