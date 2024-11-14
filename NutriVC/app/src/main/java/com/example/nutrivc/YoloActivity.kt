import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import java.io.FileInputStream
import java.io.IOException

class YoloActivity : AppCompatActivity() {

    private lateinit var interpreter: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_yolo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageView: ImageView = findViewById(R.id.imageView)
        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        imageView.setImageURI(imageUri)

        // Carregar o modelo TensorFlow Lite
        interpreter = Interpreter(loadModelFile(assetManager, "yolo.tflite"))

        // Processar a imagem e realizar a detecção
        imageUri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            val results = detectFood(bitmap)
            // Exibir os resultados na interface do usuário
        }
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor: AssetFileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel: FileChannel = inputStream.channel
        val startOffset: Long = fileDescriptor.startOffset
        val declaredLength: Long = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun detectFood(bitmap: Bitmap): List<DetectionResult> {
        // Implementar a lógica de detecção usando o modelo YOLO
        // ...
    }
}