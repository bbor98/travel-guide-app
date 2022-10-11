package com.borabor.travelguideapp.presentation.ui.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.borabor.travelguideapp.domain.model.Category
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.usecase.GetGuideCategories
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
class GuideViewModel @Inject constructor(
    private val getTravelList: GetTravelList,
    private val getGuideCategories: GetGuideCategories,
    private val updateBookmark: UpdateBookmark
) : ViewModel() {

    private val _mightNeedList = MutableLiveData(emptyList<Travel>())
    val mightNeedList: LiveData<List<Travel>> = _mightNeedList

    private val _categoryList = MutableLiveData(emptyList<Category>())
    val categoryList: LiveData<List<Category>> = _categoryList

    private val _topPickList = MutableLiveData(emptyList<Travel>())
    val topPickList: LiveData<List<Travel>> = _topPickList

    private val _bookmarkState = MutableLiveData(UiState.successState())
    val bookmarkState: LiveData<UiState> = _bookmarkState

    private val _uiState = MutableLiveData(UiState.loadingState())
    val uiState: LiveData<UiState> = _uiState

    init {
        fetchLists()
    }

    private fun fetchLists() {
        combine(
            getTravelList(ListType.MIGHT_NEEDS),
            getTravelList(ListType.TOP_PICK_ARTICLES),
            getGuideCategories()
        ) { mightNeedResponse, topPickResponse, guideCategoriesResponse ->
            if (
                mightNeedResponse is Resource.Success &&
                topPickResponse is Resource.Success &&
                guideCategoriesResponse is Resource.Success
            ) {
                _mightNeedList.value = mightNeedResponse.data
                _topPickList.value = topPickResponse.data
                _categoryList.value = guideCategoriesResponse.data
                _uiState.value = UiState.successState()
            } else {
                _uiState.value = UiState.errorState()
            }
        }.launchIn(viewModelScope)
    }

    fun updateBookmark(id: String) {
        viewModelScope.launch {
            topPickList.value?.find { it.id == id }?.let { travel ->
                updateBookmark(id, !travel.isBookmark).collect { response ->
                    when (response) {
                        is Resource.Success -> {
                            val updatedList = topPickList.value?.map { travel ->
                                if (travel.id == id) travel.copy(isBookmark = response.data.isBookmark)
                                else travel
                            }

                            _topPickList.value = updatedList
                            _bookmarkState.value = UiState.successState()
                        }
                        is Resource.Error -> {
                            _bookmarkState.value = UiState.errorState()
                        }
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