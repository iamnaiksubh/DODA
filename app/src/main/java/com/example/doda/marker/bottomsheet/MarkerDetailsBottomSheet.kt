package com.example.doda.marker.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.doda.databinding.MarkerDetailsBottomSheetBinding
import com.example.doda.marker.listener.InsertMarkerDataListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MarkerDetailsBottomSheet(private val title: String = "", private val details: String = "") :
    BottomSheetDialogFragment() {

    private lateinit var binding: MarkerDetailsBottomSheetBinding
    var insertMarkerDataListener: InsertMarkerDataListener? = null
    var showMarkerDetails = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MarkerDetailsBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (showMarkerDetails) {
            showDetails()
        } else {
            binding.save.visibility = View.VISIBLE
            binding.close.visibility = View.GONE
        }
        setOnClickListener()
    }

    private fun showDetails() {
        binding.save.visibility = View.GONE
        binding.close.visibility = View.VISIBLE
        binding.title.setText(title)
        binding.details.setText(details)
        showMarkerDetails = false
    }

    private fun setOnClickListener() {
        binding.save.setOnClickListener {
            setMarkerDetails()
        }
        binding.close.setOnClickListener {
            dismissAllowingStateLoss()
        }
    }

    private fun setMarkerDetails() {
        val title = binding.title.text.toString()
        val details = binding.details.text.toString()

        if (title.isBlank() || title.isEmpty()) {
            Toast.makeText(requireContext(), "Title can't be blank or empty", Toast.LENGTH_LONG)
                .show()
            return
        }

        if (details.isBlank() || details.isEmpty()) {
            Toast.makeText(requireContext(), "Details can't be blank or empty", Toast.LENGTH_LONG)
                .show()
            return
        }

        insertMarkerDataListener?.insertMarker(title, details)
        dismissAllowingStateLoss()
    }

}