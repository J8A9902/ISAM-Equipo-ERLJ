package com.example.vinyls.repositories

import android.app.Application
import com.example.vinyls.models.Collector
import com.example.vinyls.network.NetworkServiceAdapter

class CollectorRepository (val application: Application){
    suspend fun refreshData(): List<Collector>{
        //Determinar la fuente de datos que se va a utilizar. Si es necesario consultar la red, ejecutar el siguiente código
        return NetworkServiceAdapter.getInstance(application).getCollectors()
    }
}