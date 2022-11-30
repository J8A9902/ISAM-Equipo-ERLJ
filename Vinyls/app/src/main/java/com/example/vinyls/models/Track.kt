package com.example.vinyls.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class Track (
    @PrimaryKey val trackId:Int,
    val name:String,
    val duration:String
)
