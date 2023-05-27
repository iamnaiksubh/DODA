package com.example.doda.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.doda.R
import com.example.doda.databinding.DrawingItemBinding
import com.example.doda.listeners.OnItemClickListener
import com.example.doda.model.Drawing
import com.example.doda.viewholders.DrawingItemViewHolder

class DrawingItemAdapter(
    private val mContext: Context,
    private val items: List<Drawing>,
    private val onItemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<DrawingItemViewHolder>() {
    private lateinit var binding: DrawingItemBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawingItemViewHolder {
        val inflate = LayoutInflater.from(mContext)
        binding = DataBindingUtil.inflate(inflate, R.layout.drawing_item, parent, false)
        return DrawingItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: DrawingItemViewHolder, position: Int) {
        holder.bind(items[position])

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(items[position])
        }
    }
}