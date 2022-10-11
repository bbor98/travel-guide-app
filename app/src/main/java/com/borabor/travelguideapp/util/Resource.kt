package com.borabor.travelguideapp.util

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    object Error : Resource<Nothing>()
}