package com.borabor.travelguideapp.domain.usecase

import com.borabor.travelguideapp.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetBookmarks @Inject constructor(private val repository: BookmarkRepository) {
    operator fun invoke() = flow {
        emit(repository.getBookmarks())
    }
}