package com.example.madlevel3task2

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.fragment_portals.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class PortalsFragment : Fragment() {

    private val portals = arrayListOf<Portal>()
    private var portalAdapter: PortalAdapter =
        PortalAdapter(portals) { portal: Portal -> portalItemClicked(portal)

        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_portals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

    }
    private fun initViews() {
        // Initialize the recycler view with a linear layout manager, adapter
        rvPortals.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        rvPortals.adapter = portalAdapter
        observeAddReminderResult()
        createItemTouchHelper().attachToRecyclerView(rvPortals)
    }
    private fun observeAddReminderResult() {
        setFragmentResultListener(REQ_PORTAL_KEY) { key, bundle ->
            val portalTitle = bundle.getString(ARG_PORTAL_NAME)
            val portalUrl = bundle.getString(ARG_PORTAL_URL)
            if (!portalTitle.isNullOrBlank() && !portalUrl.isNullOrBlank()) {
                val reminder = Portal(portalTitle,portalUrl)

                portals.add(reminder)
                portalAdapter.notifyDataSetChanged()
            } else {
                Log.e(
                    "PortalsFragment",
                    "Request triggered, but empty portal fields! Portal: $portalTitle , $portalUrl"
                )
            }
        }
    }
    /**
     * Create a touch helper to recognize when a user swipes an item from a recycler view.
     * An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
     * and uses callbacks to signal when a user is performing these actions.
     */
    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                portals.removeAt(position)
                portalAdapter.notifyDataSetChanged()
            }
        }
        return ItemTouchHelper(callback)
    }

    private fun portalItemClicked(portalItem: Portal) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()

        // If context is not null launch the Chrome Custom Tabs
        // The Uri needs to begin with 'http://', else you get No Activity found exception
        context?.let { customTabsIntent.launchUrl(it, Uri.parse((portalItem.portalUrl))) }

        Toast.makeText(context, "Clicked: ${portalItem.portalUrl}", Toast.LENGTH_LONG).show()
    }

}