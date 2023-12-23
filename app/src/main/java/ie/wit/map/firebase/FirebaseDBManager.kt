package ie.wit.map.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import ie.wit.map.models.PlaceModel
import ie.wit.map.models.PlaceStore
import timber.log.Timber

object FirebaseDBManager : PlaceStore {

    var database: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun findAll(placesList: MutableLiveData<List<PlaceModel>>) {
        database.child("places")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Place error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<PlaceModel>()
                    val children = snapshot.children
                    children.forEach {
                        val place = it.getValue(PlaceModel::class.java)
                        localList.add(place!!)
                    }
                    database.child("places")
                        .removeEventListener(this)

                    placesList.value = localList
                }
            })
    }

    override fun findAll(userid: String, placesList: MutableLiveData<List<PlaceModel>>) {

        database.child("user-places").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Timber.i("Firebase Place error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<PlaceModel>()
                    val children = snapshot.children
                    children.forEach {

                        val place = it.getValue(PlaceModel::class.java)
                        Timber.i("firebase Got value ${place!!}")
                        localList.add(place!!)
                    }
                    database.child("user-places").child(userid)
                        .removeEventListener(this)

                    placesList.value = localList
                }
            })
    }

    override fun findById(userid: String, placeid: String, place: MutableLiveData<PlaceModel>) {

        database.child("user-places").child(userid)
            .child(placeid).get().addOnSuccessListener {
                place.value = it.getValue(PlaceModel::class.java)
                Timber.i("firebase Got value ${it.value}")
            }.addOnFailureListener{
                Timber.e("firebase Error getting data $it")
            }
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, place: PlaceModel) {
        Timber.i("Firebase DB Reference : $database")

        val uid = firebaseUser.value!!.uid
        val key = database.child("places").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        place.uid = key
        val placeValues = place.toMap()

        val childAdd = HashMap<String, Any>()
        childAdd["/places/$key"] = placeValues
        childAdd["/user-places/$uid/$key"] = placeValues

        database.updateChildren(childAdd)
    }

    override fun delete(userid: String, placeid: String) {

        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/places/$placeid"] = null
        childDelete["/user-places/$userid/$placeid"] = null

        database.updateChildren(childDelete)
    }

    override fun update(userid: String, placeid: String, place: PlaceModel) {

        val placeValues = place.toMap()

        val childUpdate : MutableMap<String, Any?> = HashMap()
        childUpdate["places/$placeid"] = placeValues
        childUpdate["user-places/$userid/$placeid"] = placeValues

        database.updateChildren(childUpdate)
    }

    fun updateImageRef(userid: String,imageUri: String) {

        val userPlaces = database.child("user-places").child(userid)
        val allPlaces = database.child("places")

        userPlaces.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        //Update Users imageUri
                        it.ref.child("profilepic").setValue(imageUri)
                        //Update all donations that match 'it'
                        val place = it.getValue(PlaceModel::class.java)
                        allPlaces.child(place!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }
}