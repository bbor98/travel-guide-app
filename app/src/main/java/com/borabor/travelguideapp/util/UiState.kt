package com.borabor.travelguideapp.util

data class UiState(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val isError: Boolean,
    val errorMessage: String? = null
) {
    companion object {
        fun loadingState() = UiState(isLoading = true, isSuccess = false, isError = false)
        fun successState() = UiState(isLoading = false, isSuccess = true, isError = false)
        fun errorState(message: String) = UiState(isLoading = false, isSuccess = false, isError = true, errorMessage = message)
    }
}
