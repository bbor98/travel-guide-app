package com.borabor.travelguideapp.di

import com.borabor.travelguideapp.data.repository.BookmarkRepositoryImpl
import com.borabor.travelguideapp.data.repository.CategoryRepositoryImpl
import com.borabor.travelguideapp.data.repository.TravelRepositoryImpl
import com.borabor.travelguideapp.data.repository.TripRepositoryImpl
import com.borabor.travelguideapp.domain.repository.BookmarkRepository
import com.borabor.travelguideapp.domain.repository.CategoryRepository
import com.borabor.travelguideapp.domain.repository.TravelRepository
import com.borabor.travelguideapp.domain.repository.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTravelRepository(impl: TravelRepositoryImpl): TravelRepository

    @Binds
    abstract fun bindBookmarkRepository(impl: BookmarkRepositoryImpl): BookmarkRepository

    @Binds
    abstract fun bindTripRepository(impl: TripRepositoryImpl): TripRepository

    @Binds
    abstract fun bindCategoryRepository(impl: CategoryRepositoryImpl): CategoryRepository
}