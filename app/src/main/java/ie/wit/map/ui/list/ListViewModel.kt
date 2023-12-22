package ie.wit.map.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ie.wit.map.models.PlaceManager
import ie.wit.map.models.PlaceModel
import timber.log.Timber
import java.lang.Exception

class ListViewModel : ViewModel() {

    private val placesList = MutableLiveData<List<PlaceModel>>()

    val observablePlacesList: LiveData<List<PlaceModel>>
        get() = placesList

    init {
        load()
    }

    fun load() {
        placesList.value = PlaceManager.findAll()
    }

    //fun load() {
    //    try {
    //        DonationManager.findAll(donationsList)
    //        Timber.i("Retrofit Load Success : $donationsList.value")
    //    }
    //    catch (e: Exception) {
    //        Timber.i("Retrofit Load Error : $e.message")
    //    }
    //}

    fun delete(id: String) {
        try {
            PlaceManager.delete(id)
            Timber.i("Retrofit Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Retrofit Delete Error : $e.message")
        }
    }
}