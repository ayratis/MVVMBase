package com.ayratis.abstractapp.ui.dashoard

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.ayratis.abstractapp.App
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.live_data.observeEvent
import com.ayratis.abstractapp.arch.view_model.injectViewModel
import com.ayratis.abstractapp.arch.view_model.obtainSharedVM
import com.ayratis.abstractapp.databinding.FragmentDashboardBinding
import com.ayratis.abstractapp.ui._base.BaseBindingFragment
import com.ayratis.abstractapp.ui.dashoard.ImageCropFragment.Companion.ARG_URI
import com.bumptech.glide.Glide
import java.io.File
import java.io.IOException

class DashboardFragment : BaseBindingFragment<FragmentDashboardBinding>() {

    override val layoutId: Int get() = R.layout.fragment_dashboard
    private lateinit var viewModel: DashboardViewModel

    private var currentPhotoPath: String? = null
    private lateinit var sharedPhotoVM: SharedPhotoVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = injectViewModel(App.appComponent.viewModelFactory())
        sharedPhotoVM = obtainSharedVM(activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = viewModel
        setupCommandObservers()
    }

    private fun setupCommandObservers() {
        viewModel.openCameraCommand.observeEvent(this) {
            if (allPermissionsGranted()) {
                dispatchTakePicIntent()
            } else {
                requestPermissions(REQUEST_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }

        sharedPhotoVM.imageCroppedCommand.observeEvent(this) {
            Glide.with(this).load(it).into(binding.photoImg)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                dispatchTakePicIntent()
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.permission_request_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {
        context?.run {
            for (permission in REQUEST_PERMISSIONS) {
                if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED
                ) return false
            }
            return true
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                findNavController().navigate(R.id.action_dashboardFragment_to_imageCropFragment,
                    Bundle().apply { putString(ARG_URI, currentPhotoPath) })
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${System.currentTimeMillis()}",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePicIntent() {
        context?.run {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePicIntent ->
                takePicIntent.resolveActivity(packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        null
                    }

                    photoFile?.also { file ->
                        val photoUri: Uri = FileProvider.getUriForFile(
                            this,
                            applicationContext.packageName + ".fileprovider",
                            file
                        )
                        takePicIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                        startActivityForResult(takePicIntent, REQUEST_CODE_PHOTO)
                    }
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUEST_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PHOTO = 1
    }


}
