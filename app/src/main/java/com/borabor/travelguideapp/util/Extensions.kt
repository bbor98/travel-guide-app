package com.borabor.travelguideapp.util

import android.os.Build
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.borabor.travelguideapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

fun String?.calculateDaysBetweenDates() = this?.let {
    val dtf: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("dd MMM, yyyy")
    } else return 0

    val inputString = it.split(" - ")

    val date1: LocalDate? = LocalDate.parse(inputString.first(), dtf)
    val date2: LocalDate? = LocalDate.parse(inputString.last(), dtf)

    Duration.between(date1?.atStartOfDay(), date2?.atStartOfDay()).toDays().toInt()
} ?: 0

fun UiState.handleBookmarkState(fragment: Fragment, imageView: ImageView, progressBar: ProgressBar) {
    if (isLoading) {
        imageView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    } else if (isSuccess) {
        imageView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    } else if (isError) {
        imageView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        Toast.makeText(fragment.requireContext(), fragment.getString(R.string.error_bookmark), Toast.LENGTH_SHORT).show()
    }
}