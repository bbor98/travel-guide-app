package com.borabor.travelguideapp.domain.repository

import com.borabor.travelguideapp.domain.model.Travel

interface TripRepository {
    suspend fun getTrips(): List<Travel>
    suspend fun insertTrip(trip: Travel)
    suspend fun deleteTrip(trip: Travel)
}