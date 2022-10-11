package com.borabor.travelguideapp.presentation.ui.trip

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.usecase.*
import com.borabor.travelguideapp.util.ListType
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripViewModel @Inject constructor(
    private val getTrips: GetTrips,
    private val insertTrip: InsertTrip,
    private val deleteTrip: DeleteTrip,
    private val getTravelList: GetTravelList,
    private val getBookmarks: GetBookmarks,
    private val updateBookmark: UpdateBookmark
) : ViewModel() {

    private val _tabPosition = MutableLiveData(0)
    val tabPosition: LiveData<Int> = _tabPosition

    private val _tripList = MutableLiveData(emptyList<Travel>())
    val tripList: LiveData<List<Travel>> = _tripList

    private val _travelList = MutableLiveData(emptyList<Travel>())
    val travelList: LiveData<List<Travel>> = _travelList

    private val _bookmarkList = MutableLiveData(emptyList<Travel>())
    val bookmarkList: LiveData<List<Travel>> = _bookmarkList

    private val _bookmarkState = MutableLiveData(UiState.successState())
    val bookmarkState: LiveData<UiState> = _bookmarkState

    private val _uiState = MutableLiveData(UiState.loadingState())
    val uiState: LiveData<UiState> = _uiState

    init {
        fetchTripList()
    }

    fun setTabPosition(tabPosition: Int) {
        _tabPosition.value = tabPosition
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
            coroutineScope { insertTrip(trip) }
            fetchTripList()
        }
    }

    fun removeTrip(trip: Travel) {
        viewModelScope.launch {
            coroutineScope { deleteTrip(trip) }
            fetchTripList()
        }
    }

    fun fetchTravelList() {
        viewModelScope.launch {
            getTravelList(ListType.ALL).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _travelList.value = response.data.filter {
                            it.category == "hotel" || it.category == "topdestination" || it.category == "nearby" || it.category == "mightneed"
                        }
                    }
                    is Resource.Error -> return@collect
                }
            }
        }
    }

    fun fetchBookmarkList() {
        viewModelScope.launch {
            getBookmarks().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _bookmarkList.value = response.data
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> _uiState.value = UiState.errorState()
                }
            }
        }
    }

    fun deleteBookmark(id: String) {
        viewModelScope.launch {
            updateBookmark(id, false).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        val updatedList = _bookmarkList.value?.toMutableList()
                        updatedList?.remove(response.data.copy(isBookmark = true))

                        _bookmarkList.value = updatedList
                        _bookmarkState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _bookmarkState.value = UiState.errorState()
                    }
                }
            }
        }
    }

    fun retry() {
        _uiState.value = UiState.loadingState()
        fetchBookmarkList()
    }
}