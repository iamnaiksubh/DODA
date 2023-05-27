package com.example.doda

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.doda.adapters.DrawingItemAdapter
import com.example.doda.databinding.ActivityDrawingBinding
import com.example.doda.dialogs.AddDrawingItemDialog
import com.example.doda.listeners.InsertDataListener
import com.example.doda.listeners.OnItemClickListener
import com.example.doda.marker.MarkerActivity
import com.example.doda.model.Drawing
import com.example.doda.viewmodels.DrawingViewModel

class DrawingActivity : AppCompatActivity(), OnItemClickListener, InsertDataListener {

    private lateinit var binding: ActivityDrawingBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewModel: DrawingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawing)
        recyclerView = binding.drawingItemRecyclerView
        initViewModel()
        setOnClickListener()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[DrawingViewModel::class.java]
        viewModel.allDrawings.observe(this) {
            initRecyclerView(it)
        }
    }

    private fun setOnClickListener() {
        binding.addImage.setOnClickListener {
            val bottomSheetFragment = AddDrawingItemDialog()
            bottomSheetFragment.insertDataListener = this
            bottomSheetFragment.show(supportFragmentManager, "AddDrawingItemDialog")
        }
    }

    private fun initRecyclerView(items: List<Drawing>) {
        recyclerView.layoutManager = LinearLayoutManager(this, VERTICAL, false)
        recyclerView.adapter = DrawingItemAdapter(this, items, this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, VERTICAL).apply {
                ContextCompat.getDrawable(
                    this@DrawingActivity,
                    R.drawable.recyclerview_item_divider
                )
                    ?.let { this.setDrawable(it) }
            }
        )
    }

    override fun insertData(drawing: Drawing) {
        viewModel.insert(drawing)
        viewModel.allDrawings.observe(this) { drawings ->
            recyclerView.adapter = DrawingItemAdapter(this, drawings, this)
        }
    }

    override fun onItemClick(drawing: Drawing) {
        val intent = Intent(this, MarkerActivity::class.java)
        intent.putExtra(DRAWING_IMAGE, drawing.image)
        intent.putExtra(DRAWING_ID, drawing.id)
        startActivity(intent)
    }

    companion object {
        const val DRAWING_IMAGE = "drawing_image"
        const val DRAWING_ID = "drawing_id"
    }

}