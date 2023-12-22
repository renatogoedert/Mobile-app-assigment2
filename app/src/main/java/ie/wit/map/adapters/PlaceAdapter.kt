package ie.wit.map.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.map.R
import ie.wit.map.databinding.CardPlaceBinding
import ie.wit.map.models.PlaceModel

interface PlaceClickListener {
    fun onPlaceClick(place: PlaceModel)
}

class PlaceAdapter(private var places: MutableList<PlaceModel>,
                   private val listener: PlaceClickListener)
    : RecyclerView.Adapter<PlaceAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardPlaceBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val place = places[holder.adapterPosition]
        holder.bind(place,listener)
    }

    override fun getItemCount(): Int = places.size

    fun removeAt(position: Int) {
        places.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class MainHolder(val binding : CardPlaceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(place: PlaceModel, listener: PlaceClickListener) {
            binding.place = place
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            binding.root.setOnClickListener { listener.onPlaceClick(place) }
            binding.executePendingBindings()
        }
    }
}