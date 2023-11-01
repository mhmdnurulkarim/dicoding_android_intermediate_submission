package com.example.mystoryapps.view.add_story

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
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
import com.example.mystoryapps.network.ApiConfig
import com.example.mystoryapps.utils.Conts.CAMERAX_RESULT
import com.example.mystoryapps.utils.Conts.CAMERA_PERMISSION
import com.example.mystoryapps.utils.Conts.COARSE_LOCATION_PERMISSION
import com.example.mystoryapps.utils.Conts.FINE_LOCATION_PERMISSION
import com.example.mystoryapps.utils.reduceFileImage
import com.example.mystoryapps.utils.uriToFile
import com.example.mystoryapps.view.ViewModelFactory
import com.example.mystoryapps.view.cameraX.CameraActivity
import com.example.mystoryapps.view.list_story.ListStoryActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private val addViewModel: AddStoryViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var location: Location? = null
    private var lat: RequestBody? = null
    private var lon: RequestBody? = null
    private var currentImageUri: Uri? = null

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[FINE_LOCATION_PERMISSION] ?: false -> {}
                permissions[COARSE_LOCATION_PERMISSION] ?: false -> {}
                permissions[CAMERA_PERMISSION] ?: false -> {}
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

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        addViewModel.getTokenUser().observe(this) {token ->
            ApiConfig.getApiService(token)
        }

        binding.apply {
            topAppBar.setNavigationOnClickListener {finish()}
            btnAddGalery.setOnClickListener { startGallery() }
            btnAddCamera.setOnClickListener { startCamera() }
            checkboxAdd.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    checkboxAdd.isChecked = true
                    getMyLastLocation()
                } else {
                    checkboxAdd.isChecked = false
                    location = null
                }
            }
            btnAddUpload.setOnClickListener {
                currentImageUri?.let { uri ->
                    val imageFile = uriToFile(uri, this@AddStoryActivity).reduceFileImage()
                    val description = binding.edtAddDescription.text.toString().trim()

                    onLoading()
                    val requestBodyDescription = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile,
                    )

                    if (location != null){
                        lat = location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                        lon = location?.longitude.toString().toRequestBody("text/plain".toMediaType())
                    }

                    addViewModel.addStories(multipartBody, requestBodyDescription, lat, lon).observe(this@AddStoryActivity){ result ->
                        when(result) {
                            is Result.Loading -> {
                                onLoading()
                            }
                            is Result.Success -> {
                                onSuccess()
                            }
                            is Result.Error -> {
                                message(result.error)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun checkPermission(permission: String): Boolean =
        ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        if (checkPermission(CAMERA_PERMISSION)){
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCamera.launch(intent)
        } else {
            requestPermissionLauncher.launch(
                arrayOf(CAMERA_PERMISSION, FINE_LOCATION_PERMISSION, COARSE_LOCATION_PERMISSION)
            )
        }
    }

    private fun getMyLastLocation() {
        if (checkPermission(FINE_LOCATION_PERMISSION) && checkPermission(COARSE_LOCATION_PERMISSION)){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.location = location
                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(CAMERA_PERMISSION, FINE_LOCATION_PERMISSION, COARSE_LOCATION_PERMISSION)
            )
        }
    }

    private fun showImage() {
        currentImageUri.let {
            binding.ivAddPhoto.setImageURI(it)
        }
    }

    private fun onLoading(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun message(message: String) {
        binding.progressBar.visibility = View.GONE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun onSuccess(){
        message("Upload gambar sukses")
        val intent = Intent(this@AddStoryActivity, ListStoryActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        binding.progressBar.visibility = View.GONE
    }
}