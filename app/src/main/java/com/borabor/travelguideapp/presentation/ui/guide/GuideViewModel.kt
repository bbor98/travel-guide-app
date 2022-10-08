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
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _travelUiState = MutableLiveData(UiState.loadingState())
    val travelUiState: LiveData<UiState> = _travelUiState

    private val _categoryUiState = MutableLiveData(UiState.loadingState())
    val categoryUiState: LiveData<UiState> = _categoryUiState

    init {
        fetchTravelList()
        fetchCategoryList()
    }

    private fun fetchTravelList() {
        viewModelScope.launch {
            getTravelList().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _mightNeedList.value = response.data.filter { it.category == "mightneed" }
                        _topPickList.value = response.data.filter { it.category == "toppick" }
                        _travelUiState.value = UiState.successState()
                    }
                    is Resource.Error -> _travelUiState.value = UiState.errorState(response.message)
                }
            }
        }
    }

    private fun fetchCategoryList() {
        viewModelScope.launch {
            getGuideCategories().collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _categoryList.value = response.data
                        _categoryUiState.value = UiState.successState()
                    }
                    is Resource.Error -> _categoryUiState.value = UiState.errorState(response.message)
                }
            }
        }
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
                        is Resource.Error -> _bookmarkState.value = UiState.errorState(response.message)
                    }
                }
            }
        }
    }

    fun retry() {
        _travelUiState.value = UiState.loadingState()
        fetchTravelList()
    }
}