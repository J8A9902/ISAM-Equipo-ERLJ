package com.example.vinyls.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.vinyls.database.TracksDao
import com.example.vinyls.models.Track
import com.example.vinyls.network.NetworkServiceAdapter
import org.json.JSONObject


class TrackRepository (val application: Application, private val tracksDao: TracksDao){
    suspend fun refreshData(albumId: Int): List<Track>{
        var cached = tracksDao.getTracks()
        return if(cached.isNullOrEmpty()){
            val cm = application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if( cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE){
                emptyList()
            } else NetworkServiceAdapter.getInstance(application).getTracksByAlbumId(albumId
            )
        } else cached
    }
    suspend fun createTrack(track: JSONObject, albumId: Int):Track{
        Log.d("TrackRepository","Crear Track")
        return NetworkServiceAdapter.getInstance(application).createTrack(track, albumId)
    }
}