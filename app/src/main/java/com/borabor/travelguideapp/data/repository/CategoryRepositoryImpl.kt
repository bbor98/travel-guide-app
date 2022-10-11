package com.borabor.travelguideapp.data.repository

import com.borabor.travelguideapp.data.remote.api.TravelApi
import com.borabor.travelguideapp.domain.repository.CategoryRepository
import com.borabor.travelguideapp.util.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val api: TravelApi,
    private val safeApiCall: SafeApiCall
) : CategoryRepository {
    override suspend fun getGuideCategories() = safeApiCall.execute {
        api.getGuideCategories()
    }
}