package ie.wit.map.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
//import ie.wit.map.models.PlaceManager
import ie.wit.map.firebase.FirebaseDBManager
import ie.wit.map.models.PlaceModel
import timber.log.Timber
import java.lang.Exception

class ListViewModel : ViewModel() {

    private val placesList = MutableLiveData<List<PlaceModel>>()

    val observablePlacesList: LiveData<List<PlaceModel>>
        get() = placesList

    var liveFirebaseUser = MutableLiveData<FirebaseUser>()

    init {
        load()
    }
    fun load() {
        try {
            val user = Firebase.auth.currentUser?.uid
            if (user != null) {
                FirebaseDBManager.findAll(user,placesList)
            }
            Timber.i("Report Load Success : ${placesList.value.toString()}")
        }
        catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("Report Delete Success")
        }
        catch (e: Exception) {
            Timber.i("Report Delete Error : $e.message")
        }
    }
}