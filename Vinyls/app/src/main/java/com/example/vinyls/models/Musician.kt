package com.example.vinyls.models

import org.json.JSONArray

data class Musician(
    val musicianId:Int,
    val name:String,
    val image:String,
    val description:String,
    val birthDate: String,
    val album: String,
    val performerPrize: JSONArray
)