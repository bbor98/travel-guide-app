package com.borabor.travelguideapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.usecase.GetTravelList
import com.borabor.travelguideapp.util.ListType
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getTravelList: GetTravelList) : ViewModel() {

    private val _dealList = MutableLiveData(emptyList<Travel>())
    val dealList: LiveData<List<Travel>> = _dealList

    private val _uiState = MutableLiveData(UiState.loadingState())
    val uiState: LiveData<UiState> = _uiState

    init {
        fetchDealList()
    }

    private fun fetchDealList() {
        viewModelScope.launch {
            getTravelList(ListType.DEALS).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _dealList.value = response.data
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.errorState()
                    }
                }
            }
        }
    }

    fun filterDeals(filterBy: String) {
        _dealList.value =
            if (filterBy == "all") dealList.value
            else dealList.value?.filter { it.category == filterBy }
    }

    fun retry() {
        _uiState.value = UiState.loadingState()
        fetchDealList()
    }
}