package ie.wit.map.ui.place

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.wit.map.firebase.FirebaseDBManager
import ie.wit.map.firebase.FirebaseImageManager
//import ie.wit.map.models.PlaceManager
import ie.wit.map.models.PlaceModel

class AddViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    fun addPlace(firebaseUser: MutableLiveData<FirebaseUser>,
                 place: PlaceModel) {
        status.value = try {
            //DonationManager.create(donation)
            place.profilepic = FirebaseImageManager.imageUri.value.toString()
            FirebaseDBManager.create(firebaseUser,place)

            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}