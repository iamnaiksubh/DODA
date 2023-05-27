package com.example.doda.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Drawing::class, Marker::class],
    version = 3
)
abstract class DrawingDatabase : RoomDatabase() {
    abstract val dao : DrawingDao
}