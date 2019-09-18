package com.ayratis.abstractapp.ui.dashoard

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ayratis.abstractapp.R
import com.ayratis.abstractapp.arch.live_data.call
import com.ayratis.abstractapp.arch.view_model.obtainSharedVM
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.io.IOException

class ImageCropFragment : Fragment() {
    private var uri: String? = null
    private lateinit var cropImageView: CropImageView
    private lateinit var saveButton: Button
    private lateinit var sharedPhotoVM: SharedPhotoVM
    private var destinatinationPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPhotoVM = obtainSharedVM(activity)
        arguments?.let {
            uri = it.getString(ARG_URI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_crop, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cropImageView = view.findViewById(R.id.crop_image)
        saveButton = view.findViewById(R.id.save)

        saveButton.isEnabled = false

        Glide.with(this).asBitmap().load(uri).into(object : CustomTarget<Bitmap>(720, 1280) {
            override fun onLoadCleared(placeholder: Drawable?) {
            }
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                cropImageView.setImageBitmap(resource)
                saveButton.isEnabled = true
            }
        })

        saveButton.setOnClickListener {
            getDestinationPhotoUri()?.let{
                cropImageView.saveCroppedImageAsync(it)
            }
        }

        cropImageView.setOnCropImageCompleteListener { _,_ ->
            destinatinationPhotoPath?.let {
                sharedPhotoVM.imageCroppedCommand.call(it)
                findNavController().popBackStack()
            }
        }
    }

    @Throws(IOException::class)
    private fun createDestinationFile(): File {
        val storageDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${System.currentTimeMillis()}",
            ".jpg",
            storageDir
        ).apply {
            destinatinationPhotoPath = absolutePath
        }
    }

    private fun getDestinationPhotoUri(): Uri? {

        val destinationFile: File? = try {
            createDestinationFile()
        } catch (ex: IOException) {
            null
        }

        destinationFile?.also { file ->
            val photoUri: Uri? = context?.let {
                FileProvider.getUriForFile(
                    it,
                    it.applicationContext.packageName + ".fileprovider",
                    file
                )
            }
            return photoUri
        }
        return null
    }

    companion object {
        const val ARG_URI = "photo_uri"
    }
}
