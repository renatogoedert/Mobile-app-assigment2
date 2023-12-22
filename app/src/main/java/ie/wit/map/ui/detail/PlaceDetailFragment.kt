package ie.wit.map.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import ie.wit.map.R

class PlaceDetailFragment : Fragment() {

    companion object {
        fun newInstance() = PlaceDetailFragment()
    }

    private lateinit var viewModel: PlaceDetailViewModel
    private val args by navArgs<PlaceDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_place_detail, container, false)


        Toast.makeText(context,"Place ID Selected : ${args. placeid}",Toast.LENGTH_LONG).show()

        return view
    }


}