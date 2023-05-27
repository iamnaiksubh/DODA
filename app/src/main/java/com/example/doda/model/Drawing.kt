package com.example.doda.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Drawing(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageName: String,
    val image: ByteArray,
    val timeOfCreation: Long
)
