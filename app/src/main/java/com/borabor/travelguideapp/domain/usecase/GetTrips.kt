package com.borabor.travelguideapp.domain.usecase

import com.borabor.travelguideapp.domain.repository.TripRepository
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTrips @Inject constructor(private val repository: TripRepository) {
    operator fun invoke() = flow {
        emit(repository.getTrips())
    }
}