package com.borabor.travelguideapp.data.repository

import com.borabor.travelguideapp.data.local.dao.TravelDao
import com.borabor.travelguideapp.data.remote.api.TravelApi
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.repository.TravelRepository
import com.borabor.travelguideapp.util.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelRepositoryImpl @Inject constructor(
    private val api: TravelApi,
    private val safeApiCall: SafeApiCall,
    private val dao: TravelDao
) : TravelRepository {
    override suspend fun getTravelList() = safeApiCall.execute {
        api.getTravelList()
    }

    override suspend fun getGuideCategories() = safeApiCall.execute {
        api.getGuideCategories()
    }

    override suspend fun updateBookmark(id: String, isBookmark: Boolean) = safeApiCall.execute {
        api.updateBookmark(id, isBookmark)
    }

    override suspend fun getTrips(): List<Travel> = dao.getTrips()

    override suspend fun insertTrip(trip: Travel) {
        dao.insertTrip(trip)
    }

    override suspend fun deleteTrip(trip: Travel) {
        dao.deleteTrip(trip)
    }
}