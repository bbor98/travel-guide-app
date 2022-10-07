package com.borabor.travelguideapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.borabor.travelguideapp.data.local.converter.ImageListConverter
import com.borabor.travelguideapp.data.local.dao.TravelDao
import com.borabor.travelguideapp.domain.model.Travel

@Database(entities = [Travel::class], version = 1)
@TypeConverters(ImageListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun travelDao(): TravelDao
}