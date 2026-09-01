package com.example.synapseapp

import android.app.Application
import viewModel.GadgetInfo

class MyApp : Application() {
    val gadgetInfo = GadgetInfo()
}
