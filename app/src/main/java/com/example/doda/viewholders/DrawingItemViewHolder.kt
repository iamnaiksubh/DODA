package com.example.doda.viewholders

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.doda.databinding.DrawingItemBinding
import com.example.doda.model.Drawing
import java.util.Calendar

class DrawingItemViewHolder(private val binding: DrawingItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Drawing) {
        binding.name.text = item.imageName
        binding.additionTime.text = getTimeAgoString(item.timeOfCreation)
        binding.thumbnail.setImageBitmap(convertByteArrayToBitmap(item.image))
    }

    private fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun getTimeAgoString(timeOfCreation: Long): String {
        val currentTimeMillis = System.currentTimeMillis()
        val diffMillis = currentTimeMillis - timeOfCreation
        val seconds = diffMillis / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "A few seconds ago"
            minutes < 60 -> "$minutes minutes ago"
            hours < 24 -> "$hours hours ago"
            days == 1L -> "Yesterday"
            days < 30 -> "$days days ago"
            else -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timeOfCreation
                val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                "At ${formatHour(hourOfDay)}:${formatMinute(minute)}"
            }
        }
    }

    private fun formatHour(hour: Int): String {
        return if (hour < 10) "0$hour" else hour.toString()
    }

    private fun formatMinute(minute: Int): String {
        return if (minute < 10) "0$minute" else minute.toString()
    }

}