package com.example.vinyls.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vinyls.models.Musician

@Dao
interface MusiciansDao {

    @Query("SELECT * FROM musician_table")
    fun getMusicians():List<Musician>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(musician: Musician)

    @Query("DELETE FROM musician_table")
    suspend fun deleteAll():Int
}