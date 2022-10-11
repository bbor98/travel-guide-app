package com.borabor.travelguideapp.presentation.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.usecase.GetTravelList
import com.borabor.travelguideapp.domain.usecase.UpdateBookmark
import com.borabor.travelguideapp.util.ListType
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getTravelList: GetTravelList,
    private val updateBookmark: UpdateBookmark
) : ViewModel() {

    private val _topDestList = MutableLiveData(emptyList<Travel>())
    val topDestList: LiveData<List<Travel>> = _topDestList

    private val _nearbyList = MutableLiveData(emptyList<Travel>())
    val nearbyList: LiveData<List<Travel>> = _nearbyList

    private val _bookmarkState = MutableLiveData<UiState?>(null)
    val bookmarkState: LiveData<UiState?> = _bookmarkState

    private val _uiState = MutableLiveData(UiState.loadingState())
    val uiState: LiveData<UiState> = _uiState

    init {
        fetchLists()
    }

    private fun fetchLists() {
        combine(
            getTravelList(ListType.TOP_DESTINATIONS),
            getTravelList(ListType.NEARBY_ATTRACTIONS)
        ) { topDestResponse, nearbyResponse ->
            if (
                topDestResponse is Resource.Success &&
                nearbyResponse is Resource.Success
            ) {
                _topDestList.value = topDestResponse.data
                _nearbyList.value = nearbyResponse.data
                _uiState.value = UiState.successState()
            } else {
                _uiState.value = UiState.errorState()
            }
        }.launchIn(viewModelScope)
    }

    fun bookmark(id: String, isBookmark: Boolean) {
        _bookmarkState.value = UiState.loadingState()

        viewModelScope.launch {
            updateBookmark(id, !isBookmark).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        val updatedList = nearbyList.value?.map { travel ->
                            if (travel.id == id) travel.copy(isBookmark = response.data.isBookmark)
                            else travel
                        }

                        _nearbyList.value = updatedList
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
        fetchLists()
    }
}