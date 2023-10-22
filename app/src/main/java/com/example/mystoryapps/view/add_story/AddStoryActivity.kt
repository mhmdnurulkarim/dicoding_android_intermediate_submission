package com.example.mystoryapps.view.add_story

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.mystoryapps.data.Result
import com.example.mystoryapps.databinding.ActivityAddStoryBinding
import com.example.mystoryapps.utils.Conts.CAMERAX_RESULT
import com.example.mystoryapps.utils.Conts.REQUIRED_PERMISSION
import com.example.mystoryapps.utils.reduceFileImage
import com.example.mystoryapps.utils.uriToFile
import com.example.mystoryapps.view.ViewModelFactory
import com.example.mystoryapps.view.cameraX.CameraActivity
import com.example.mystoryapps.view.list_story.ListStoryActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val addViewModel: AddStoryViewModel by viewModels { factory }

        if (!allPermissionsGranted()) { requestPermissionLauncher.launch(REQUIRED_PERMISSION) }

        binding.apply {
            btnAddGalery.setOnClickListener { startGallery() }
            btnAddCamera.setOnClickListener { startCamera() }
            btnAddUpload.setOnClickListener {
                currentImageUri?.let { uri ->
                    val imageFile = uriToFile(uri, this@AddStoryActivity).reduceFileImage()
                    val description = binding.edtAddDescription.text.toString().trim()

                    onLoading()
                    val requestBody = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile
                    )

                    addViewModel.getTokenUser().observe(this@AddStoryActivity) { token ->
                        addViewModel.addStories(token.toString(), multipartBody, requestBody).observe(this@AddStoryActivity){ result ->
                            when(result) {
                                is Result.Loading -> {
                                    onLoading()
                                }
                                is Result.Success -> {
                                    onSuccess()
                                }
                                is Result.Error -> {
                                    onError(result.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private fun showImage() {
        currentImageUri.let {
            binding.ivAddPhoto.setImageURI(it)
        }
    }

    private fun onLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onError(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(){
        onError("Upload gambar sukses")
        startActivity(Intent(this@AddStoryActivity, ListStoryActivity::class.java))
        finishAffinity()
        binding.progressBar.visibility = View.GONE
    }
}