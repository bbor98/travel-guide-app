package com.borabor.travelguideapp.presentation.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.borabor.travelguideapp.domain.usecase.UpdateBookmark
import com.borabor.travelguideapp.presentation.base.BaseViewModel
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val updateBookmark: UpdateBookmark) : BaseViewModel() {

    private val _isBookmark = MutableLiveData<Boolean>()
    val isBookmark: LiveData<Boolean?> get() = _isBookmark

    private val _imagePosition = MutableLiveData(0)
    val imagePosition: LiveData<Int> get() = _imagePosition

    fun bookmark(id: String, isBookmark: Boolean) {
        viewModelScope.launch {
            updateBookmark(id, !isBookmark).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _isBookmark.value = response.data.isBookmark
                        _bookmarkState.value = UiState.successState()
                    }
                    is Resource.Error -> {
                        _bookmarkState.value = UiState.errorState()
                    }
                }
            }
        }
    }

    fun setImagePosition(imagePosition: Int?) {
        _imagePosition.value = imagePosition
    }
}