package com.example.vinyls.repositories

import android.app.Application
import com.android.volley.VolleyError
import com.example.vinyls.models.Musician
import com.example.vinyls.network.NetworkServiceAdapter


class MusicianRepository (val application: Application){
    suspend fun refreshData(): List<Musician>{
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getMusicians()
    }
}