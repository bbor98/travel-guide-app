package com.borabor.travelguideapp.di

import com.borabor.travelguideapp.data.repository.TravelRepositoryImpl
import com.borabor.travelguideapp.domain.repository.TravelRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTravelRepository(impl: TravelRepositoryImpl): TravelRepository
}