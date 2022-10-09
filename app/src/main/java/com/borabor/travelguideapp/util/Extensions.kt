package com.borabor.travelguideapp.util

import android.view.View
import androidx.fragment.app.Fragment
import com.borabor.travelguideapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNav() {
    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)?.visibility = View.GONE
}

fun Fragment.showBottomNav() {
    requireActivity().findViewById<BottomNavigationView>(R.id.bottomNav)?.visibility = View.VISIBLE
}