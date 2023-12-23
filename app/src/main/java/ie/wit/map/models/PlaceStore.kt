package ie.wit.map.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface PlaceStore {
    fun findAll(placesList:
                MutableLiveData<List<PlaceModel>>)
    fun findAll(userid:String,
                placesList:
                MutableLiveData<List<PlaceModel>>
    )
    fun findById(userid:String, placeid: String,
                 place: MutableLiveData<PlaceModel>)
    fun create(firebaseUser: MutableLiveData<FirebaseUser>, donation: PlaceModel)
    fun delete(userid:String, placeid: String)
    fun update(userid:String, placeid: String, place: PlaceModel)
}