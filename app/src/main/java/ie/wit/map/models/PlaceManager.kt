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

    override fun findById(id:Long) : PlaceModel? {
        val foundPlace: PlaceModel? = places.find { it.id == id }
        return foundPlace
    }

    override fun create(place: PlaceModel) {
        place.id = getId()
        places.add(place)
        logAll()
    }

    fun logAll() {
        Timber.v("** Place List **")
        places.forEach { Timber.v("Place ${it}") }
    }
}