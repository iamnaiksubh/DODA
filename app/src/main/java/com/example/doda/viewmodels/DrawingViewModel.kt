package com.example.doda.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.doda.model.Drawing
import com.example.doda.model.DrawingDatabase
import com.example.doda.model.Marker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DrawingViewModel(
    application: Application
) : AndroidViewModel(application) {

    val allDrawings: LiveData<List<Drawing>>

    private var db: DrawingDatabase = Room.databaseBuilder(
        application.applicationContext,
        DrawingDatabase::class.java, "drawing",
    ).fallbackToDestructiveMigration().build()

    init {
        allDrawings = db.dao.getDrawings()
    }

    fun insert(drawing: Drawing) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dao.insertDrawing(drawing)
        }

        db.dao.getDrawings().value?.let { updatedDrawings ->
            (allDrawings as MutableLiveData<List<Drawing>>).postValue(updatedDrawings)
        }
    }

    fun insertMarker(marker: Marker) {
        viewModelScope.launch(Dispatchers.IO) {
            db.dao.insertMarker(marker)
        }
    }

    fun getMarkers(id: Int): LiveData<List<Marker>> {
        return db.dao.getMarkers(id)
    }

    fun getClickedMarker(x: Float, y: Float): LiveData<Marker> {
        return db.dao.getClickedMarker(x, y)
    }

}