package ie.wit.map.models

interface PlaceStore {
    fun findAll() : List<PlaceModel>
    fun findById(id: Long) : PlaceModel?
    fun create(place: PlaceModel)
}