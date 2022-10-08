package com.borabor.travelguideapp.domain.usecase

import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.domain.repository.TravelRepository
import javax.inject.Inject

class InsertTrip @Inject constructor(private val repository: TravelRepository) {
    suspend operator fun invoke(trip: Travel) {
        repository.insertTrip(trip)
    }
}