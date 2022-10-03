package com.borabor.travelguideapp.domain.model

data class Travel(
    val category: String,
    val city: String,
    val country: String,
    val description: String,
    val id: String,
    val images: List<Image>,
    val title: String
)