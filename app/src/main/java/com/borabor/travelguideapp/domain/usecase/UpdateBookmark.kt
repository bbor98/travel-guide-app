package com.borabor.travelguideapp.domain.usecase

import com.borabor.travelguideapp.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateBookmark @Inject constructor(private val repository: BookmarkRepository) {
    operator fun invoke(id: String, isBookmark: Boolean) = flow {
        emit(repository.updateBookmark(id, isBookmark))
    }
}