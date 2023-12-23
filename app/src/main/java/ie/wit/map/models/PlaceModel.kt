package ie.wit.map.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize


@Parcelize
data class PlaceModel(var uid: String = "N/A",
                      val rating: String = "N/A",
                      val country: String = "N/A",
                      var profilepic: String = "",
                      val email: String = "joe@bloggs.com") : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "rating" to rating,
            "country" to country,
            "profilepic" to profilepic,
            "email" to email
        )
    }
}
