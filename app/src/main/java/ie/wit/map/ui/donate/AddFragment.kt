package ie.wit.map.ui.donate

import android.os.Bundle
import android.widget.ArrayAdapter
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import ie.wit.map.R
import ie.wit.map.databinding.FragmentAddPlaceBinding
import ie.wit.map.models.PlaceModel
import ie.wit.map.ui.list.ListViewModel

class AddFragment : Fragment() {

    var totalDonated = 0
    private var _fragBinding: FragmentAddPlaceBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val fragBinding get() = _fragBinding!!
    //lateinit var navController: NavController
    private lateinit var addViewModel: AddViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _fragBinding = FragmentAddPlaceBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_donate)
	    setupMenu()
        addViewModel = ViewModelProvider(this).get(AddViewModel::class.java)
        addViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                status -> status?.let { render(status) }
        })

        // Use the country names for the Spinner
        val countrySpinner = fragBinding.countrySpinner
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.country_names,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            countrySpinner.adapter = adapter
        }

        setButtonListener(fragBinding)
        return root
    }

 private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_donate, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


//    companion object {
//        @JvmStatic
//        fun newInstance() =
//                DonateFragment().apply {
//                    arguments = Bundle().apply {}
//                }
//    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.donationError),Toast.LENGTH_LONG).show()
        }
    }

    fun setButtonListener(layout: FragmentAddPlaceBinding) {
        layout.donateButton.setOnClickListener {
            val selectedCountry = layout.countrySpinner.selectedItem as String
            //if(totalDonated >= layout.progressBar.max)
            //    Toast.makeText(context,"Donate Amount Exceeded!", Toast.LENGTH_LONG).show()
            //else {
            val paymentmethod = when (layout.paymentMethod.checkedRadioButtonId) {
                R.id.star3 -> "3"
                R.id.star2 -> "2"
                else -> "1"
            }
                totalDonated += 10
                layout.totalSoFar.text = getString(R.string.totalSoFar,totalDonated)
                layout.progressBar.progress = totalDonated
                addViewModel.addPlace(PlaceModel(rating = paymentmethod,country = selectedCountry))
           // }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        val reportViewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        reportViewModel.observableDonationsList.observe(viewLifecycleOwner, Observer {
                totalDonated = 100
                    //reportViewModel.observableDonationsList.value!!.sumOf { it.amount }
        })
        fragBinding.progressBar.progress = totalDonated
        fragBinding.totalSoFar.text = getString(R.string.totalSoFar,totalDonated)
    }
}