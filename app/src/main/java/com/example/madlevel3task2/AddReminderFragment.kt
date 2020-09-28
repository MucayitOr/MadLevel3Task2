package com.example.madlevel3task2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_reminder.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */

const val REQ_REMINDER_KEY = "req_reminder"
const val ARG_PORTAL_NAME = "arg_portal_name"
const val ARG_PORTAL_URL = "arg_portal_url"

class AddReminderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnAddReminder.setOnClickListener {
            onAddReminder()
        }
    }
    private fun onAddReminder() {
        val reminderText = etReminderName.text.toString()
        val reminderText2 = etReminderName2.text.toString()

        if (reminderText.isNotBlank() && reminderText2.isNotBlank()) {
            //set the data as fragmentResult, we are listening for REQ_REMINDER_KEY in RemindersFragment!
            setFragmentResult(REQ_REMINDER_KEY, bundleOf(Pair(ARG_PORTAL_NAME, reminderText), Pair(
                ARG_PORTAL_URL, reminderText2)))

            //"pop" the backstack, this means we destroy
            //this fragment and go back to the RemindersFragment
            findNavController().popBackStack()

        } else {
            Toast.makeText(
                activity,
                R.string.not_valid_reminder, Toast.LENGTH_SHORT
            ).show()
        }
    }

}