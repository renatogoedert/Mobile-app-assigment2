package ie.wit.map.models

interface PlaceStore {
    fun findAll() : List<PlaceModel>
    fun findById(id: String) : PlaceModel?
    fun create(place: PlaceModel)
    fun delete(id: String)
}