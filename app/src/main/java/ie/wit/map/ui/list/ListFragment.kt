package ie.wit.map.ui.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ie.wit.map.R
import ie.wit.map.adapters.PlaceAdapter
import ie.wit.map.adapters.PlaceClickListener
import ie.wit.map.databinding.FragmentListPlaceBinding
import ie.wit.map.firebase.FirebaseDBManager
import ie.wit.map.main.MapApp
import ie.wit.map.models.PlaceModel
import ie.wit.map.ui.auth.LoggedInViewModel
import ie.wit.map.utils.SwipeToDeleteCallback
import ie.wit.map.utils.SwipeToPhotoCallback
import ie.wit.map.utils.createLoader
import ie.wit.map.utils.hideLoader
import ie.wit.map.utils.showLoader


class ListFragment : Fragment(), PlaceClickListener {

    lateinit var app: MapApp
    private var _fragBinding: FragmentListPlaceBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val reportViewModel: ListViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var loader : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentListPlaceBinding.inflate(inflater, container, false)
        val root = fragBinding.root
	setupMenu()
        loader = createLoader(requireActivity())

        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        showLoader(loader,"Downloading Places")
        reportViewModel.observablePlacesList.observe(viewLifecycleOwner, Observer {
                places ->
            places?.let  {
                render(places as ArrayList<PlaceModel>)
                hideLoader(loader)
                checkSwipeRefresh()
            }
        })

        fragBinding.fab.setOnClickListener {
            val action = ListFragmentDirections.actionListFragmentToPlaceFragment()
            findNavController().navigate(action)
        }

        setSwipeRefresh()


        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                showLoader(loader,"Deleting Place")
                val adapter = fragBinding.recyclerView.adapter as PlaceAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                val user = Firebase.auth.currentUser?.uid
                if (user != null) {
                    reportViewModel.delete(user,
                        (viewHolder.itemView.tag as PlaceModel).uid!!)
                }

                hideLoader(loader)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToPhotoCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val action = ListFragmentDirections.actionListFragmentToCameraFragment("")
                findNavController().navigate(action)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)


        return root
    }

   private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                // Handle for example visibility of menu items
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_report, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Validate and handle the selected menu item
                return NavigationUI.onNavDestinationSelected(menuItem,
                    requireView().findNavController())
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun render(placesList: MutableList<PlaceModel>) {
        fragBinding.recyclerView.adapter = PlaceAdapter(placesList,this)
        if (placesList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.placesNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.placesNotFound.visibility = View.GONE
        }
    }

    override fun onPlaceClick(place: PlaceModel) {
        place.isfav = !place.isfav!!

        val user = Firebase.auth.currentUser?.uid
        if (user != null) {
            FirebaseDBManager.update(user, place.uid, place)
        }
    }

    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Places")
            reportViewModel.load()
        }
    }

    fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    override fun onResume() {
        super.onResume()
        reportViewModel.load()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }
}