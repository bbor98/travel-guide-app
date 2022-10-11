package com.borabor.travelguideapp.data.remote.api

import com.borabor.travelguideapp.domain.model.Category
import com.borabor.travelguideapp.domain.model.Travel
import retrofit2.http.*

interface TravelApi {
    @GET("travel_list")
    suspend fun getTravelList(@Query("category") category: String? = null): List<Travel>

    @GET("guide_categories")
    suspend fun getGuideCategories(): List<Category>

    @FormUrlEncoded
    @PUT("travel_list/{id}")
    suspend fun updateBookmark(
        @Path("id") id: String,
        @Field("isBookmark") isBookmark: Boolean
    ): Travel
}