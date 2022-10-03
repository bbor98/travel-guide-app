package com.borabor.travelguideapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TravelDTO(
    @SerializedName("category")
    val category: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("country")
    val country: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("images")
    val images: List<ImageDTO>,
    @SerializedName("isBookmark")
    val isBookmark: Boolean,
    @SerializedName("title")
    val title: String
)