package com.example.doda.marker

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doda.R
import com.example.doda.databinding.ActivityMarkerBinding
import com.example.doda.marker.bottomsheet.MarkerDetailsBottomSheet
import com.example.doda.marker.listener.InsertMarkerDataListener
import com.example.doda.marker.listener.OnMarkerClickListener
import com.example.doda.model.Marker
import com.example.doda.viewmodels.DrawingViewModel
import kotlin.math.ceil


class MarkerActivity : AppCompatActivity(), InsertMarkerDataListener, OnMarkerClickListener {

    private lateinit var binding: ActivityMarkerBinding
    private lateinit var gestureDetector: GestureDetector
    private lateinit var imageBitmap: Bitmap
    private var drawingId: Int = -1
    private lateinit var markerImage: ImageView
    private lateinit var viewModel: DrawingViewModel
    private var markerDetailsBottomSheet: MarkerDetailsBottomSheet? = null
    private var markerX = 0F
    private var markerY = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_marker)
        gestureDetector = GestureDetector(this, object : SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                markerX = e.x
                markerY = e.y
                if (markerDetailsBottomSheet?.isVisible == true)
                    markerDetailsBottomSheet?.dismiss()
                markerDetailsBottomSheet = MarkerDetailsBottomSheet()
                markerDetailsBottomSheet?.insertMarkerDataListener = this@MarkerActivity
                markerDetailsBottomSheet?.showMarkerDetails = false
                markerDetailsBottomSheet?.show(
                    supportFragmentManager,
                    "AddMarkerDetailsBottomSheet"
                )
                return true
            }
        })
        setToolBar()
        setValues()
        initViewModel()
        setOnClickListener()
    }

    private fun setToolBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[DrawingViewModel::class.java]
        viewModel.getMarkers(drawingId).observe(this, Observer {
            if (it.isEmpty())
                return@Observer
            showMarkers(it)
        })
    }

    private fun showMarkers(markers: List<Marker>) {
        for (marker in markers) {
            addMarker(marker.x, marker.y)
        }
    }

    private fun setValues() {
        imageBitmap =
            intent.getByteArrayExtra(DRAWING_IMAGE)?.let { convertByteArrayToBitmap(it) }!!
        drawingId = intent.getIntExtra(DRAWING_ID, -1)
        binding.imageView.setImageBitmap(imageBitmap)

        markerImage = ImageView(this).apply {
            setImageResource(R.drawable.ic_marker_icon)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setOnClickListener() {
        binding.imageView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event!!)
    }

    private fun addMarker(x: Float, y: Float) {
        val markerImage = MarkerImageView(this, x, y, this)

        val markerLayoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        val xPixels = ceil(x.toDouble()).toInt()
        val yPixels = ceil(y.toDouble()).toInt()

        val markerWidth = markerImage.drawable.intrinsicWidth
        val markerHeight = markerImage.drawable.intrinsicHeight
        val leftMargin = xPixels - markerWidth / 2
        val topMargin = yPixels - markerHeight

        markerLayoutParams.leftMargin = leftMargin
        markerLayoutParams.topMargin = topMargin

        binding.parentLayout.addView(markerImage, markerLayoutParams)
    }

    private fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    override fun insertMarker(title: String, details: String) {
        viewModel.insertMarker(Marker(0, drawingId, markerX, markerY, title, details))
        addMarker(markerX, markerY)
        markerDetailsBottomSheet?.dismiss()
    }

    override fun onMarkerClick(x: Float, y: Float) {
        viewModel.getClickedMarker(x, y).observe(this) {
                markerDetailsBottomSheet = MarkerDetailsBottomSheet(it.title, it.details)
                markerDetailsBottomSheet?.showMarkerDetails = true
                markerDetailsBottomSheet?.show(supportFragmentManager, "ShowMarkerDetailsBottomSheet")
        }
        if (markerDetailsBottomSheet?.showMarkerDetails == false) markerDetailsBottomSheet?.dismiss()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val DRAWING_IMAGE = "drawing_image"
        const val DRAWING_ID = "drawing_id"
    }

}