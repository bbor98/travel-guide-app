package com.borabor.travelguideapp.domain.usecase

import com.borabor.travelguideapp.domain.repository.TravelRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTravelList @Inject constructor(private val repository: TravelRepository) {
    operator fun invoke() = flow {
        emit(repository.getTravelList())
    }
}