package ie.wit.map.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PlaceModel(var uid: String = "N/A",
                      val rating: String = "N/A",
                      val country: String = "N/A") : Parcelable