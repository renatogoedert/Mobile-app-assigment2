package ie.wit.map.ui.donate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.map.models.PlaceManager
import ie.wit.map.models.PlaceModel

class AddViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addPlace(place: PlaceModel) {
        status.value = try {
            PlaceManager.create(place)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}