package com.example.doda.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface DrawingDao {

    @Insert
    fun insertDrawing(drawing: Drawing)

    @Update
    fun updateDrawing(drawing: Drawing)

    @Insert
    fun insertMarker(marker: Marker)

    @Query("select * from marker where x = :markerX and y = :markerY")
    fun getClickedMarker(markerX: Float, markerY: Float): LiveData<Marker>

    @Query("select * from marker where drawingId = :id")
    fun getMarkers(id: Int): LiveData<List<Marker>>

    @Query("select * from drawing order by id DESC")
    fun getDrawings(): LiveData<List<Drawing>>
}