package com.borabor.travelguideapp.data.remote.api

import com.borabor.travelguideapp.data.remote.dto.CategoryDTO
import com.borabor.travelguideapp.data.remote.dto.TravelDTO
import retrofit2.http.*

interface TravelApi {
    @GET("travel_list")
    suspend fun getTravelList(): List<TravelDTO>

    @GET("guide_categories")
    suspend fun getGuideCategories(): List<CategoryDTO>

    @FormUrlEncoded
    @PUT("travel_list/{id}")
    suspend fun updateBookmark(
        @Path("id") id: String,
        @Field("isBookmark") isBookmark: Boolean
    ): TravelDTO
}