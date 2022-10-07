package com.borabor.travelguideapp.di

import android.content.Context
import androidx.room.Room
import com.borabor.travelguideapp.data.local.dao.TravelDao
import com.borabor.travelguideapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideTravelDao(database: AppDatabase): TravelDao = database.travelDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "travel-app-database"
    ).build()
}