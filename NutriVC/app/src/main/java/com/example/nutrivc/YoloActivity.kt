package com.example.nutrivc

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileNotFoundException
import java.io.InputStream
import com.example.nutrivc.Constants.MODEL_PATH
import com.example.nutrivc.Constants.LABELS_PATH

class YoloActivity : AppCompatActivity() {

    private lateinit var overlayView: OverlayView
    private var detectedObjects: List<BoundingBox> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yolo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageView: ImageView = findViewById(R.id.imageView)
        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        imageView.setImageURI(imageUri)

        overlayView = findViewById(R.id.overlay_view)

        imageUri?.let {
            processImage(it)
        }

        val buttonSendAnother: Button = findViewById(R.id.button_send_another)
        buttonSendAnother.setOnClickListener {
            // Lógica para enviar outra foto
            val intent = Intent(this, YoloActivity::class.java)
            startActivity(intent)
        }

        val buttonContinue: Button = findViewById(R.id.button_continue)
        buttonContinue.setOnClickListener {
            // Lógica para continuar para ResultadoActivity
            val intent = Intent(this, ResultadoActivity::class.java)
            intent.putParcelableArrayListExtra("detectedObjects", ArrayList(detectedObjects))
            startActivity(intent)
        }
    }

    private fun processImage(imageUri: Uri) {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            val detector = Detector(this, MODEL_PATH, LABELS_PATH, object : Detector.DetectorListener {
                override fun onEmptyDetect() {
                    println("Nenhum objeto detectado")
                }

                override fun onDetect(boundingBoxes: List<BoundingBox>, inferenceTime: Long) {
                    detectedObjects = boundingBoxes
                    for (box in boundingBoxes) {
                        println("Objeto: ${box.clsName}, Confiança: ${box.cnf}")
                    }
                    overlayView.setResults(boundingBoxes)
                }
            }) { message ->
                println(message)
            }

            detector.detect(bitmap)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }
}