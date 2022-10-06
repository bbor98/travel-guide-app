package com.borabor.travelguideapp.presentation.ui.search

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
class SearchViewModel @Inject constructor(
    private val getTravelList: GetTravelList,
    // TODO
    /*private val addBookmark: AddBookmark,
    private val deleteBookmark: DeleteBookmark,*/
) : ViewModel() {

    private val _topDestList = MutableLiveData(emptyList<Travel>())
    val topDestList: LiveData<List<Travel>> = _topDestList

    private val _nearbyList = MutableLiveData(emptyList<Travel>())
    val nearbyList: LiveData<List<Travel>> = _nearbyList

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
                        _topDestList.value = response.data.filter { it.category == "topdestination" }
                        _nearbyList.value = response.data.filter { it.category == "nearby" }
                        _uiState.value = UiState.successState()
                    }
                    is Resource.Error -> _uiState.value = UiState.errorState(response.message)
                }
            }
        }
    }

    fun bookmark(id: String) {
        // TODO
        /*val isBookmarked = nearbyList.value?.first { it.id == id }.isBookmark
        if (isBookmarked) deleteBookmark(id) else addBookmark()*/
    }

    fun retry() {
        _uiState.value = UiState.loadingState()
        fetchTravelList()
    }
}