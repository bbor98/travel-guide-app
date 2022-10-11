package com.borabor.travelguideapp.data.repository

import com.borabor.travelguideapp.data.remote.api.TravelApi
import com.borabor.travelguideapp.domain.repository.TravelRepository
import com.borabor.travelguideapp.util.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelRepositoryImpl @Inject constructor(
    private val api: TravelApi,
    private val safeApiCall: SafeApiCall
) : TravelRepository {
    override suspend fun getAllTravelList() = safeApiCall.execute {
        api.getTravelList()
    }

    override suspend fun getDeals() = safeApiCall.execute {
        api.getTravelList("flight|hotel|transportation")
    }

    override suspend fun getTopDestinations() = safeApiCall.execute {
        api.getTravelList("topdestination")
    }

    override suspend fun getNearbyAttractions() = safeApiCall.execute {
        api.getTravelList("nearby")
    }

    override suspend fun getMightNeeds() = safeApiCall.execute {
        api.getTravelList("mightneed")
    }

    override suspend fun getTopPickArticles() = safeApiCall.execute {
        api.getTravelList("toppick")
    }
}