package com.borabor.travelguideapp.domain.repository

import com.borabor.travelguideapp.domain.model.Travel
import com.borabor.travelguideapp.util.Resource

interface TravelRepository {
    suspend fun getAllTravelList(): Resource<List<Travel>>
    suspend fun getDeals(): Resource<List<Travel>>
    suspend fun getTopDestinations(): Resource<List<Travel>>
    suspend fun getNearbyAttractions(): Resource<List<Travel>>
    suspend fun getTripLocations(): Resource<List<Travel>>
    suspend fun getMightNeeds(): Resource<List<Travel>>
    suspend fun getTopPickArticles(): Resource<List<Travel>>
}