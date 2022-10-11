package com.borabor.travelguideapp.domain.repository

import com.borabor.travelguideapp.domain.model.Category
import com.borabor.travelguideapp.util.Resource

interface CategoryRepository {
    suspend fun getGuideCategories(): Resource<List<Category>>
}