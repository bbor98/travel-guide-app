package com.borabor.travelguideapp.data.repository

import com.borabor.travelguideapp.data.remote.api.TravelApi
import com.borabor.travelguideapp.domain.repository.BookmarkRepository
import com.borabor.travelguideapp.util.SafeApiCall
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookmarkRepositoryImpl @Inject constructor(
    private val api: TravelApi,
    private val safeApiCall: SafeApiCall
) : BookmarkRepository {
    override suspend fun getBookmarks() = safeApiCall.execute {
        api.getTravelList("bookmark")
    }

    override suspend fun updateBookmark(id: String, isBookmark: Boolean) = safeApiCall.execute {
        api.updateBookmark(id, isBookmark)
    }
}