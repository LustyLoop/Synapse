package com.example.synapseapp

import android.app.Application
import com.example.synapseapp.room.MainDb
import viewModel.GadgetInfo

class MyApp : Application() {
    val gadgetInfo = GadgetInfo()
    val database by lazy { MainDb.createDataBase(this) }
}
