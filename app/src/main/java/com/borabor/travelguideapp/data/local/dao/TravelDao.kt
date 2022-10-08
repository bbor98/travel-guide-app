package com.borabor.travelguideapp.data.local.dao

import androidx.room.*
import com.borabor.travelguideapp.domain.model.Travel

@Dao
interface TravelDao {
    @Query("SELECT * FROM travel ORDER BY dateCreated")
    suspend fun getTrips(): List<Travel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: Travel)

    @Delete
    suspend fun deleteTrip(trip: Travel)
}