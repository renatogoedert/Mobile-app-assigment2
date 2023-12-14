package ie.wit.map.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PlaceModel(var id: Long = 0,
                      val paymentmethod: String = "N/A",
                      val amount: String = "N/A") : Parcelable