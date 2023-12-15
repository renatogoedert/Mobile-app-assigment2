package ie.wit.map.models

import timber.log.Timber

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

object PlaceManager : PlaceStore {

    private val places = ArrayList<PlaceModel>()

    override fun findAll(): List<PlaceModel> {
        return places
    }

    override fun findById(id:String) : PlaceModel? {
        val foundPlace: PlaceModel? = places.find { it.uid == id }
        return foundPlace
    }

    override fun create(place: PlaceModel) {
        place.uid = getId().toString()
        places.add(place)
        logAll()
    }

    fun logAll() {
        Timber.v("** Place List **")
        places.forEach { Timber.v("Place ${it}") }
    }

    override fun delete(id: String) {
        val placeToRemove = places.find { it.uid == id }
        placeToRemove?.let {
            places.remove(it)
            logAll()
        }
    }
}