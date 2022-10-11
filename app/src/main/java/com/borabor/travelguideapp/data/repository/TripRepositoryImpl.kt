package com.borabor.travelguideapp.data.repository

import com.borabor.travelguideapp.data.local.dao.TravelDao
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.repository.TripRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepositoryImpl @Inject constructor(private val dao: TravelDao) : TripRepository {
    override suspend fun getTrips(): List<Travel> = dao.getTrips()

    override suspend fun insertTrip(trip: Travel) {
        dao.insertTrip(trip)
    }

    override suspend fun deleteTrip(trip: Travel) {
        dao.deleteTrip(trip)
    }
}