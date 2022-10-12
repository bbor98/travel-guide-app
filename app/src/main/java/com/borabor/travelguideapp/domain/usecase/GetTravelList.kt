package com.borabor.travelguideapp.domain.usecase

import com.borabor.travelguideapp.domain.repository.TravelRepository
import com.borabor.travelguideapp.util.ListType
import com.borabor.travelguideapp.util.ListType.*
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTravelList @Inject constructor(private val repository: TravelRepository) {
    operator fun invoke(listType: ListType) = flow {
        emit(
            with(repository) {
                when (listType) {
                    ALL -> getAllTravelList()
                    DEALS -> getDeals()
                    TOP_DESTINATIONS -> getTopDestinations()
                    NEARBY_ATTRACTIONS -> getNearbyAttractions()
                    TRIP_LOCATIONS -> getTripLocations()
                    MIGHT_NEEDS -> getMightNeeds()
                    TOP_PICK_ARTICLES -> getTopPickArticles()
                }
            }
        )
    }
}