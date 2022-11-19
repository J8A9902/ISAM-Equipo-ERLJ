package com.example.vinyls.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray

@Entity(tableName = "musician_table")
data class Musician(
    @PrimaryKey val musicianId:Int,
    val name:String,
    val image:String,
    val description:String,
    val birthDate: String
)