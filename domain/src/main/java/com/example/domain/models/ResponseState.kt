package com.example.domain.models

sealed class ResponseState {
    data object Loading : ResponseState()
    data object Waiting : ResponseState()
    data object Success : ResponseState()
    data class Error(
        val description: String
    ) : ResponseState()
}