package com.borabor.travelguideapp.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.borabor.travelguideapp.util.UiState

abstract class BaseViewModel : ViewModel() {

    protected val _uiState = MutableLiveData(UiState.loadingState())
    val uiState: LiveData<UiState> get() = _uiState

    protected val _bookmarkState = MutableLiveData<UiState?>(null)
    val bookmarkState: LiveData<UiState?> get() = _bookmarkState

    fun retryConnection(action: () -> Unit) {
        _uiState.value = UiState.loadingState()
        action()
    }
}