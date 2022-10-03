package com.borabor.travelguideapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryDTO(
    @SerializedName("icon")
    val icon: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String
)