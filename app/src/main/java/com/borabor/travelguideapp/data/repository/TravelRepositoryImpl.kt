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
    override suspend fun getTravelList() = safeApiCall.execute {
        api.getTravelList()
    }

    override suspend fun getGuideCategories() = safeApiCall.execute {
        api.getGuideCategories()
    }

    override suspend fun updateBookmark(id: String, isBookmark: Boolean) = safeApiCall.execute {
        api.updateBookmark(id, isBookmark)
    }
}