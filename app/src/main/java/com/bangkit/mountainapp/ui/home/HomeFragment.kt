package com.bangkit.mountainapp.ui.home

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.mountainapp.R
import com.bangkit.mountainapp.data.local.UserPreference
import com.bangkit.mountainapp.databinding.FragmentHomeBinding
import com.bangkit.mountainapp.helper.setProfilePicture
import com.bangkit.mountainapp.ml.ConvertedModelGunung2
import com.bangkit.mountainapp.ui.*
import com.bumptech.glide.Glide
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var preference: UserPreference

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        binding.iconCameraScan.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
            startCameraX()
        }

        binding.scanFromCamera.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
            startCameraX()
        }

        binding.iconGalleryScan.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
            startGallery()
        }
        binding.scanFromGallery.setOnClickListener {
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            }
            startGallery()
        }
        return root
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
                    requireActivity(),
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                requireActivity().finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity().baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        preference = UserPreference.getInstance(requireContext().dataStore)

        viewModel.getUser(preference).observe(viewLifecycleOwner) { user ->
            if (user.isLogin) {
                with(binding) {
                    greeting.text = getString(R.string.welcoming, user.username)
                    usernameProfile.text = user.username
                    val urlRandomAvatar = setProfilePicture(user.username)

                    Glide.with(requireActivity())
                        .load(urlRandomAvatar)
                        .placeholder(R.drawable.ic_baseline_person_24)
                        .error(R.mipmap.ic_launcher_round)
                        .into(imageProfile)
                }

            }
        }

        viewModel.getTop2FavMount()

        viewModel.responseGet2Fav.observe(viewLifecycleOwner) {
            var i = 0
            for (list2TopMount in it.listMountDetail) {
                i++
                if (i == 1) {
                    binding.apply {
                        tvMountainName1.text = list2TopMount.mountainName
                        tvLoc1.text = list2TopMount.location
                    }
                } else if (i == 2) {
                    binding.apply {
                        tvMountainName2.text = list2TopMount.mountainName
                        tvLoc2.text = list2TopMount.location
                    }
                } else if (i > 2) {
                    Toast.makeText(requireActivity(), "Test sampe sini", Toast.LENGTH_SHORT).show()
                    break
                }
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(requireActivity(), CameraActivity::class.java)
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
            val resized = Bitmap.createScaledBitmap(result, 300, 300, false)

            classifyImage(resized)


//            binding.previewImageView.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireActivity())
            val bitmap = fileToBitmap(myFile)
            val resized = Bitmap.createScaledBitmap(bitmap, 300, 300, false)

            classifyImage(resized)

//            binding..setImageURI(selectedImg)
        }
    }

    private fun classifyImage(bitmap: Bitmap) {
        val model = ConvertedModelGunung2.newInstance(requireContext())

        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 300, 300, 3), DataType.FLOAT32)


        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * 300 * 300 * 3) // tanya ka alfan
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
//        Toast.makeText(requireActivity(), classes[maxPos], Toast.LENGTH_SHORT).show()

        showCustomDialog(classes[maxPos], bitmap)
        // Releases model resources if no longer used.
        model.close()

    }

    private fun showCustomDialog(resultMount: String, bitmap: Bitmap) {
        val dialog = Dialog(requireActivity())
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true)
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.result_scan)
        val button: Button = dialog.findViewById(R.id.btn_detail)
        val back: Button = dialog.findViewById(R.id.btn_go_back)
        val result: TextView = dialog.findViewById(R.id.result_mountain)
        val previewImageView: ImageView = dialog.findViewById(R.id.previewImageView)
        //Initializing the views of the dialog.

        result.text = getString(R.string.gunung_1s, resultMount)
        previewImageView.setImageBitmap(bitmap)

        button.setOnClickListener{
            Toast.makeText(requireActivity(), "bisaa yeaeyyyy", Toast.LENGTH_SHORT).show()
        }
        back.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}