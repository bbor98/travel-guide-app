package com.borabor.travelguideapp.domain.repository

import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.util.Resource

interface BookmarkRepository {
    suspend fun getBookmarks(): Resource<List<Travel>>
    suspend fun updateBookmark(id: String, isBookmark: Boolean): Resource<Travel>
}