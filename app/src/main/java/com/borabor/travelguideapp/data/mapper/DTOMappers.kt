package com.borabor.travelguideapp.data.mapper

import com.borabor.travelguideapp.data.remote.dto.CategoryDTO
import com.borabor.travelguideapp.data.remote.dto.ImageDTO
import com.borabor.travelguideapp.data.remote.dto.TravelDTO
import com.borabor.travelguideapp.domain.model.Category
import com.borabor.travelguideapp.domain.model.Image
import com.borabor.travelguideapp.domain.model.Travel

fun CategoryDTO.toCategory() = Category(
    icon = icon,
    id = id,
    title = title
)

fun ImageDTO.toImage() = Image(url = url)

fun TravelDTO.toTravel() = Travel(
    category = category,
    city = city,
    country = country,
    description = description,
    id = id,
    images = images.map { it.toImage() },
    isBookmark = isBookmark,
    title = title
)