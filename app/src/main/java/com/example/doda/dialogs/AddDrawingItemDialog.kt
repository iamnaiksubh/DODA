package com.example.doda.dialogs

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.doda.databinding.DialogAddDrawingItemBinding
import com.example.doda.listeners.InsertDataListener
import com.example.doda.model.Drawing
import java.io.ByteArrayOutputStream

class AddDrawingItemDialog : DialogFragment() {

    private lateinit var binding: DialogAddDrawingItemBinding
    private var imageBitmap: Bitmap? = null
    var insertDataListener: InsertDataListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddDrawingItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWindowAttribute()
        setOnClickListener()
    }

    private fun setWindowAttribute() {
        val window = dialog?.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setOnClickListener() {
        binding.chooseImage.setOnClickListener {
            selectImageFromGallery()
        }

        binding.save.setOnClickListener {
            uploadData()
        }

        binding.close.setOnClickListener {
            dialog?.dismiss()
        }
    }

    private fun uploadData() {
        if (binding.inputText.text.isNullOrEmpty() || binding.inputText.text.isNullOrBlank()) {
            Toast.makeText(
                requireContext(),
                "Image Name Can't Be Empty or Blank",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (imageBitmap == null) {
            Toast.makeText(requireContext(), "Image Can't Be Empty", Toast.LENGTH_LONG).show()
            return
        }

        val byteArrayOutputStream = ByteArrayOutputStream()
        imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageBytes = byteArrayOutputStream.toByteArray()


        insertDataListener?.insertData(
            Drawing(
                0, binding.inputText.text.toString(),
                imageBytes, System.currentTimeMillis()
            )
        )

        dialog?.dismiss()
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            val imageUri = data.data
            imageBitmap =
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)
        } else {
            Toast.makeText(requireContext(), "Image Can't Be Empty", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val REQUEST_CODE_SELECT_IMAGE = 1
    }

}