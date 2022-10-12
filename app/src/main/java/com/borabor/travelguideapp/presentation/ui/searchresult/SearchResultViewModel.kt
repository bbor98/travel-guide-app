package com.borabor.travelguideapp.presentation.ui.searchresult

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.usecase.GetTravelList
import com.borabor.travelguideapp.presentation.base.BaseViewModel
import com.borabor.travelguideapp.presentation.ui.home.HomeFragment
import com.borabor.travelguideapp.util.ListType
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(private val getTravelList: GetTravelList) : BaseViewModel() {

    private var travelList = emptyList<Travel>()

    private val _resultList = MutableLiveData<List<Travel>?>(null)
    val resultList: LiveData<List<Travel>?> = _resultList

    fun getResults(listType: ListType, dealType: HomeFragment.DealType, query: String) {
        viewModelScope.launch {
            if (listType == ListType.ALL) fetchSearchResults(query)
            else fetchList(listType, dealType)
        }
    }

    private fun fetchSearchResults(query: String) {
        viewModelScope.launch {
            getTravelList(ListType.ALL).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        travelList = response.data
                        queryList(query)
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.errorState()
                    }
                }
            }
        }
    }

    private fun queryList(query: String) {
        _resultList.value = travelList.filter {
            it.category.contains(query) ||
                    it.title.lowercase().contains(query) ||
                    it.country.lowercase().contains(query) ||
                    it.city.lowercase().contains(query) ||
                    it.description.lowercase().contains(query)
        }
    }

    private fun fetchList(listType: ListType, dealType: HomeFragment.DealType) {
        viewModelScope.launch {
            getTravelList(listType).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _resultList.value =
                            if (listType == ListType.MIGHT_NEEDS) response.data
                            else response.data.filter { it.category == dealType.category }

                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _uiState.value = UiState.errorState()
                    }
                }
            }
        }
    }
}