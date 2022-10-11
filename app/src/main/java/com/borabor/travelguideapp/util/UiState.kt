package com.borabor.travelguideapp.util

data class UiState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val isError: Boolean
) {
    companion object {
        fun loadingState() = UiState(isLoading = true, isSuccess = false, isError = false)
        fun successState() = UiState(isLoading = false, isSuccess = true, isError = false)
        fun errorState() = UiState(isLoading = false, isSuccess = false, isError = true)
    }
}
