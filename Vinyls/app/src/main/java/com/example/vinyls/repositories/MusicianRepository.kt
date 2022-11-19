package com.example.vinyls.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.example.vinyls.database.MusiciansDao
import com.example.vinyls.models.Musician
import com.example.vinyls.network.NetworkServiceAdapter


class MusicianRepository (val application: Application, private val musiciansDao: MusiciansDao){
    suspend fun refreshData(): List<Musician>{
        var cached = musiciansDao.getMusicians()
        return if(cached.isNullOrEmpty()){
            val cm = application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if( cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE){
                emptyList()
            } else NetworkServiceAdapter.getInstance(application).getMusicians()
        } else cached
    }
}