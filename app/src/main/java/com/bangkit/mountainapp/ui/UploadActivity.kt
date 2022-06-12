package com.bangkit.mountainapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bangkit.mountainapp.databinding.ActivityUploadBinding
import com.bangkit.mountainapp.ml.ConvertedModelGunung2
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        binding.cameraXButton.setOnClickListener { startCameraX() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this@UploadActivity,
                "com.dicoding.picodiploma.mycamera",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun uploadImage() {
        Toast.makeText(this, "Fitur ini belum tersedia", Toast.LENGTH_SHORT).show()
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.previewImageView.setImageBitmap(result)
        }
    }

    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)

            val result =  BitmapFactory.decodeFile(myFile.path)
//            Silakan gunakan kode ini jika mengalami perubahan rotasi
//            val result = rotateBitmap(
//                BitmapFactory.decodeFile(myFile.path),
//                true
//            )

            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@UploadActivity)
            val bitmap = fileToBitmap(myFile)
            val resized = Bitmap.createScaledBitmap(bitmap, 300, 300, false)

            val result = classifyImage(resized)
//            val result = outputGenerator(resized)

//            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            binding.previewImageView.setImageURI(selectedImg)
        }

//    fun outputGenerator(bitmap: Bitmap){
//        //declearing tensorflow lite model variable
//        val model = ConvertedModelGunung2.newInstance(this)
//
//        // Creates inputs for reference.
//        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 300, 300, 3), DataType.FLOAT32)
//
//        val byteBuffer : ByteBuffer = ByteBuffer.allocateDirect(4*256*256*3) // tanya ka alfan
//        byteBuffer.order(ByteOrder.nativeOrder())
//
//        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, false)
//        val pixelValues = IntArray(256 * 256)
//        bitmap.getPixels(pixelValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//
//        var pixel = 0
//        for (i in 0 until 256) {
//            for (j in 0 until 256) {
//                val pixelValue = pixelValues[pixel++]
//                byteBuffer.putFloat((pixelValue shr 16 and 0xFF) / 255f)
//                byteBuffer.putFloat((pixelValue shr 8 and 0xFF) / 255f)
//                byteBuffer.putFloat((pixelValue and 0xFF) / 255f)
//            }
//        }
//
//        inputFeature0.loadBuffer(byteBuffer)
//
//        // Runs model inference and gets result.
//        val outputs = model.process(inputFeature0)
//        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
//        print(outputFeature0)
//        // Releases model resources if no longer used.
//        model.close()
//    }

//        private fun outputGenerator2(bitmap: Bitmap){
//            val image = TensorImage.fromBitmap(bitmap)
//            val option = ObjectDetector.ObjectDetectorOptions.builder()
//                .setMaxResults(1)
//                .setScoreThreshold(0.3f)
//                .build()
//            val detector = ImageClassifier.createFromFileAndOptions(
//                this,
//                "converted_model_gunung_2.tflite",
//                option)
//
//            val result = detector.detect(image)
//
//        }
//
//
//        private fun getModelByteBuffer(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
//            val fileDescriptor = assetManager.openFd(modelPath)
//            val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//            val fileChannel = inputStream.channel
//            val startOffset = fileDescriptor.startOffset
//            val declaredLength = fileDescriptor.declaredLength
//            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//        }
//
//        private fun getLabels(assetManager: AssetManager, labelPath: String): List<String> {
//            val labels = ArrayList<String>()
//            val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
//            while (true) {
//                val label = reader.readLine() ?: break
//                labels.add(label)
//            }
//            reader.close()
//            return labels
//        }
//
//        fun recognize(bitmap: Bitmap): List<Recognition>{
//            val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, false)
//            val pixelValues = IntArray(256 * 256)
//            bitmap.getPixels(pixelValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//
//            var pixel = 0
//            for (i in 0 until 256) {
//                for (j in 0 until 256) {
//                    val pixelValue = pixelValues[pixel++]
//                    byteBuffer.putFloat((pixelValue shr 16 and 0xFF) / 255f)
//                    byteBuffer.putFloat((pixelValue shr 8 and 0xFF) / 255f)
//                    byteBuffer.putFloat((pixelValue and 0xFF) / 255f)
//                }
//            }
//            val results = Array(BATCH_SIZE) { FloatArray(labels.size) }
//            model.run(byteBuffer, results)
//            return parseResults(results)
//        }
//
//        private fun parseResults(result: Array<FloatArray>): List<Recognition> {
//            val recognitions = mutableListOf<Recognition>()
//            labels.forEachIndexed { index, label ->
//                val probability = result[0][index]
//                recognitions.add(Recognition(label, probability))
//            }
//
//            return recognitions.sortedByDescending { it.probability }
//        }
//
//     */
//    @Throws(IOException::class)
//    private fun getLabels(assetManager: AssetManager, labelPath: String): List<String> {
//        val labels = ArrayList<String>()
//        val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
//        while (true) {
//            val label = reader.readLine() ?: break
//            labels.add(label)
//        }
//        reader.close()
//        return labels
//    }
//    @Throws(IOException::class)
//    private fun getModelByteBuffer(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
//        val fileDescriptor = assetManager.openFd(modelPath)
//        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
//        val fileChannel = inputStream.channel
//        val startOffset = fileDescriptor.startOffset
//        val declaredLength = fileDescriptor.declaredLength
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
//    }

}

    private fun classifyImage(bitmap: Bitmap) {
        val model = ConvertedModelGunung2.newInstance(applicationContext)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 300, 300, 3), DataType.FLOAT32)


        val byteBuffer : ByteBuffer = ByteBuffer.allocateDirect(4*300*300*3) // tanya ka alfan
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixelValues = IntArray(300 * 300)
        bitmap.getPixels(pixelValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0

        for (i in 0 until 300) {
            for (j in 0 until 300) {
                val pixelValue = pixelValues[pixel++]
                byteBuffer.putFloat((pixelValue shr 16 and 0xFF) / 255f)
                byteBuffer.putFloat((pixelValue shr 8 and 0xFF) / 255f)
                byteBuffer.putFloat((pixelValue and 0xFF) / 255f)
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val confidences = outputFeature0.floatArray
        // find the index of the class with the biggest confidence.
        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val classes = arrayOf("Bromo", "Ijen", "Papandayan", "Kerinci", "Tangkuban Perahu")

//        result.setText(classes[maxPos])
        Toast.makeText(this, classes[maxPos], Toast.LENGTH_SHORT).show()

        // Releases model resources if no longer used.
        model.close()

    }

    private fun outputGenerator(bitmap: Bitmap): String {
        //declearing tensorflow lite model variable
        val model = ConvertedModelGunung2.newInstance(this)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 300, 300, 3), DataType.FLOAT32)

        val byteBuffer : ByteBuffer = ByteBuffer.allocateDirect(4*256*256*3) // tanya ka alfan
        byteBuffer.order(ByteOrder.nativeOrder())

        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, false)
        val pixelValues = IntArray(256 * 256)
        bitmap.getPixels(pixelValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        var pixel = 0
        for (i in 0 until 256) {
            for (j in 0 until 256) {
                val pixelValue = pixelValues[pixel++]
                byteBuffer.putFloat((pixelValue shr 16 and 0xFF) / 255f)
                byteBuffer.putFloat((pixelValue shr 8 and 0xFF) / 255f)
                byteBuffer.putFloat((pixelValue and 0xFF) / 255f)
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer as String
//        print(outputFeature0)
        // Releases model resources if no longer used.
        model.close()
        return outputFeature0
    }
}
