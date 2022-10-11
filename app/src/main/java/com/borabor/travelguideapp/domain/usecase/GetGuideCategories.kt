package com.borabor.travelguideapp.domain.usecase

import com.borabor.travelguideapp.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGuideCategories @Inject constructor(private val repository: CategoryRepository) {
    operator fun invoke() = flow {
        emit(repository.getGuideCategories())
    }
}