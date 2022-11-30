package com.example.vinyls.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vinyls.models.Track

@Dao
interface TracksDao {

    @Query("SELECT * FROM track_table")
    fun getTracks():List<Track>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(track: Track)

    @Query("DELETE FROM track_table")
    suspend fun deleteAll():Int
}