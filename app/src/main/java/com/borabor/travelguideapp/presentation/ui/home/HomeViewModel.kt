package com.borabor.travelguideapp.presentation.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.usecase.GetTravelList
import com.borabor.travelguideapp.presentation.base.BaseViewModel
import com.borabor.travelguideapp.util.ListType
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getTravelList: GetTravelList) : BaseViewModel() {

    private val _dealList = MutableLiveData(emptyList<Travel>())
    val dealList: LiveData<List<Travel>> = _dealList

    private var responseData = emptyList<Travel>()

    init {
        fetchDealList()
    }

    fun fetchDealList() {
        viewModelScope.launch {
            getTravelList(ListType.DEALS).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        responseData = response.data
                        _dealList.value = responseData
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
            if (filterBy == "all") responseData
            else responseData.filter { it.category == filterBy }
    }

    private var tabPosition = 0

    fun setTabPosition(tabPosition: Int) {
        this.tabPosition = tabPosition
    }

    fun getTabPosition() = tabPosition
}