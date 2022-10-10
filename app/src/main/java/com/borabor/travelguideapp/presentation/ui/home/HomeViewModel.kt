package com.borabor.travelguideapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.usecase.GetTravelList
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getTravelList: GetTravelList) : ViewModel() {

    private var responseData = emptyList<Travel>()

    private val _travelList = MutableLiveData(emptyList<Travel>())
    val travelList: LiveData<List<Travel>> = _travelList

    private val _uiState = MutableLiveData(UiState.loadingState())
    val uiState: LiveData<UiState> = _uiState

    init {
        fetchTravelList()
    }

    private fun fetchTravelList() {
        viewModelScope.launch {
            getTravelList().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        responseData = response.data
                        _travelList.value = responseData

                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> _uiState.value = UiState.errorState(response.message)
                }
            }
        }
    }

    fun filterDeals(filterBy: String) {
        _travelList.value =
            if (filterBy == "all") responseData.filter { it.category == "flight" || it.category == "hotel" || it.category == "transportation" }
            else responseData.filter { it.category == filterBy }
    }

    fun retry() {
        _uiState.value = UiState.loadingState()
        fetchTravelList()
    }
}