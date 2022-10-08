package com.borabor.travelguideapp.presentation.ui.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.usecase.*
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val getTrips: GetTrips,
    private val insertTrip: InsertTrip,
    private val deleteTrip: DeleteTrip,
    private val getTravelList: GetTravelList,
    private val updateBookmark: UpdateBookmark
) : ViewModel() {

    private val _tripList = MutableLiveData(emptyList<Travel>())
    val tripList: LiveData<List<Travel>> = _tripList

    private val _travelList = MutableLiveData(emptyList<Travel>())
    val travelList: LiveData<List<Travel>> = _travelList

    private val _bookmarkState = MutableLiveData(UiState.successState())
    val bookmarkState: LiveData<UiState> = _bookmarkState

    private val _uiState = MutableLiveData(UiState.loadingState())
    val uiState: LiveData<UiState> = _uiState

    init {
        fetchTripList()
    }

    fun fetchTripList() {
        viewModelScope.launch {
            getTrips().collect { tripList ->
                _tripList.value = tripList
            }
        }
    }

    fun addTrip(trip: Travel) {
        viewModelScope.launch {
            insertTrip(trip)
        }
    }

    fun removeTrip(trip: Travel) {
        viewModelScope.launch {
            deleteTrip(trip)
        }
    }

    fun fetchTravelList(showOnlyBookmarks: Boolean) {
        viewModelScope.launch {
            getTravelList().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _travelList.value =
                            if (showOnlyBookmarks) response.data.filter { it.isBookmark }
                            else response.data.filter {
                                it.category == "hotel" || it.category == "topdestination" || it.category == "nearby"
                            }

                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> _uiState.value = UiState.errorState(response.message)
                }
            }
        }
    }

    fun deleteBookmark(id: String) {
        viewModelScope.launch {
            travelList.value?.find { it.id == id }?.let { travel ->
                updateBookmark(id, false).collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            val updatedList = travelList.value?.map { travel ->
                                if (travel.id == id) travel.copy(isBookmark = response.data.isBookmark)
                                else travel
                            }

                            _travelList.value = updatedList
                            _bookmarkState.value = UiState.successState()
                        }
                        is Resource.Error -> _bookmarkState.value = UiState.errorState(response.message)
                    }
                }
            }
        }
    }

    fun retry() {
        _uiState.value = UiState.loadingState()
        fetchTravelList(true)
    }
}