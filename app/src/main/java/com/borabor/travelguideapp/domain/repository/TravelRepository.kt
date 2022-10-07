package com.borabor.travelguideapp.domain.repository

import com.borabor.travelguideapp.domain.model.Category
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.util.Resource

interface TravelRepository {
    suspend fun getTravelList(): Resource<List<Travel>>
    suspend fun getGuideCategories(): Resource<List<Category>>
    suspend fun updateBookmark(id: String, isBookmark: Boolean): Resource<Travel>
    suspend fun getTrips(): List<Travel>
    suspend fun insertTrip(trip: Travel)
    suspend fun deleteTrip(trip: Travel)
}