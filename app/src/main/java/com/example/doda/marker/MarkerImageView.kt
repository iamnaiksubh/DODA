package com.example.doda.marker

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.widget.AppCompatImageView
import com.example.doda.R
import com.example.doda.marker.listener.OnMarkerClickListener

@SuppressLint("ViewConstructor")
class MarkerImageView(
    context: Context,
    x: Float,
    y: Float,
    private val onMarkerClickListener: OnMarkerClickListener
) : AppCompatImageView(context) {

    init {
        setImageResource(R.drawable.ic_marker_icon)
        setOnClickListener {
            onMarkerClickListener.onMarkerClick(x, y)
        }
    }
}
