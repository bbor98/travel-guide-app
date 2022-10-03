package com.borabor.travelguideapp.data.repository

import com.borabor.travelguideapp.data.mapper.toCategory
import com.borabor.travelguideapp.data.mapper.toTravel
import com.borabor.travelguideapp.data.remote.api.TravelApi
import com.borabor.travelguideapp.domain.model.Category
import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.repository.TravelRepository
import com.borabor.travelguideapp.util.Resource
import com.borabor.travelguideapp.util.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TravelRepositoryImpl @Inject constructor(
    private val api: TravelApi,
    private val safeApiCall: SafeApiCall
) : TravelRepository {
    override suspend fun getTravelList(): Resource<List<Travel>> = safeApiCall.execute {
        api.getTravelList().map { it.toTravel() }
    }

    override suspend fun getGuideCategories(): Resource<List<Category>> = safeApiCall.execute {
        api.getGuideCategories().map { it.toCategory() }
    }
}