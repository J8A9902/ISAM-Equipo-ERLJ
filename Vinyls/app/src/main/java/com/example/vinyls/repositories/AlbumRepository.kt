package com.example.vinyls.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.example.vinyls.database.AlbumsDao
import com.example.vinyls.models.Album
import com.example.vinyls.network.NetworkServiceAdapter
import org.json.JSONObject


class AlbumRepository (val application: Application, private val albumsDao: AlbumsDao){
    suspend fun refreshData(): List<Album>{
        Log.d("Prueba Corutina","Antes Refrescar Album")
        var cached = albumsDao.getAlbums()
        return if(cached.isNullOrEmpty()){
            val cm = application.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if( cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_WIFI && cm.activeNetworkInfo?.type != ConnectivityManager.TYPE_MOBILE){
                emptyList()
            } else NetworkServiceAdapter.getInstance(application).getAlbums()
        } else cached
    }

    suspend fun createAlbum(album: JSONObject):Album{
        Log.d("Prueba Corutina","Crear Album")
        return NetworkServiceAdapter.getInstance(application).createAlbum(album)
    }
}