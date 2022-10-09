package com.borabor.travelguideapp.util

import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.borabor.travelguideapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNav() {
    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)?.visibility = View.GONE
}

fun Fragment.showBottomNav() {
    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)?.visibility = View.VISIBLE
}

fun EditText.checkQueryTextAndProceed(fragment: Fragment, action: (String) -> Unit) {
    val query = text.toString().lowercase()

    if (query.length < 3) {
        Toast.makeText(fragment.requireContext(), fragment.getText(R.string.error_search_query), Toast.LENGTH_SHORT).show()
        return
    } else action(query)
}