package viewModel
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

class GadgetInfo: ViewModel() {
    var batteryCharge by mutableIntStateOf(67)
}

