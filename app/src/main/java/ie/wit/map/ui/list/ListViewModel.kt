package ie.wit.map.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.map.models.PlaceManager
import ie.wit.map.models.PlaceModel

class ListViewModel : ViewModel() {

    private val donationsList = MutableLiveData<List<PlaceModel>>()

    val observableDonationsList: LiveData<List<PlaceModel>>
        get() = donationsList

    init {
        load()
    }

    fun load() {
        donationsList.value = PlaceManager.findAll()
    }
}