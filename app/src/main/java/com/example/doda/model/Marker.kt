package com.example.doda.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Marker(
    @PrimaryKey(autoGenerate = true)
    val markerId : Int = 0,
    val drawingId : Int,
    val x : Float,
    val y : Float,
    val title : String,
    val details : String
)