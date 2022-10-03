package com.borabor.travelguideapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ImageDTO(
    @SerializedName("altText")
    val altText: String?,
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)