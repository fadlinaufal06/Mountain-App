package com.bangkit.mountainapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.databinding.ActivityFeedUploadBinding
import com.bangkit.mountainapp.databinding.ActivityUploadBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.io.File
import com.bangkit.mountainapp.helper.Utils
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class FeedUploadActivity : AppCompatActivity() {

    private var getFile: File? = null

    private lateinit var binding: ActivityFeedUploadBinding

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.btnGallery.setOnClickListener { startGallery() }
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

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == UploadActivity.CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.imgStoryAdd.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@FeedUploadActivity)
            val bitmap = fileToBitmap(myFile)
            val resized = Bitmap.createScaledBitmap(bitmap, 300, 300, false)

//            val result = outputGenerator(resized)
//            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            binding.imgStoryAdd.setImageURI(selectedImg)
        }
    }

//    private fun uploadStory() {
//        if (getFile != null) {
//            val file = reduceFileImage(getFile as File)
//            val inputDesc = binding.edtDescription.text.toString()
//            if (inputDesc.isNotEmpty()) {
//                val description = inputDesc.toRequestBody("text/plain".toMediaType())
//                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
//                    "photo",
//                    file.name,
//                    requestImageFile
//                )
//                viewModel.getUser().observe(this) { user ->
//                    if (user.isLogin) {
//                        viewModel.addStory(user.token, imageMultipart, description, location)
//                    } else {
//                        startActivity(Intent(this, LoginActivity::class.java))
//                        finish()
//                    }
//                }
//            } else {
//                Toast.makeText(
//                    this@AddStoryActivity,
//                    getString(R.string.please_fill_in_the_description),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        } else {
//            Toast.makeText(
//                this@AddStoryActivity,
//                getString(R.string.no_picture_attached),
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
}