package com.example.vinyls.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.vinyls.database.VinylRoomDatabase
import com.example.vinyls.models.Album
import com.example.vinyls.network.NetworkServiceAdapter
import com.example.vinyls.repositories.AlbumRepository
import com.example.vinyls.repositories.MusicianRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumListViewModel(application: Application) :  AndroidViewModel(application) {
        private val albumRepository = AlbumRepository(application, VinylRoomDatabase.getDatabase(application.applicationContext).albumsDao())

        private val _albums = MutableLiveData<List<Album>>()

        val albums: LiveData<List<Album>>
            get() = _albums

        private var _eventNetworkError = MutableLiveData<Boolean>(false)

        val eventNetworkError: LiveData<Boolean>
            get() = _eventNetworkError

        private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

        val isNetworkErrorShown: LiveData<Boolean>
            get() = _isNetworkErrorShown

        init {
            refreshDataFromNetwork()
        }

        private fun refreshDataFromNetwork() {
            try {
                viewModelScope.launch(Dispatchers.Default) {
                    withContext(Dispatchers.IO) {
                        Log.d("Prueba Corutina", "Antes AlbumRepository")
                        var data = albumRepository.refreshData()
                        Log.d("Prueba Corutina", "Despues AlbumRepository")
                        _albums.postValue(data)
                        Log.d("Prueba Corutina", "Despues de asignar valor")
                    }
                    _eventNetworkError.postValue(false)
                     _isNetworkErrorShown.postValue(false)
                }
            }
            catch (e:Exception){ //se procesa la excepcion
                Log.d("Error", e.toString())
                _eventNetworkError.postValue(true)
            }
        }

        fun onNetworkErrorShown() {
            _isNetworkErrorShown.value = true
        }

        class Factory(val app: Application) : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AlbumListViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return AlbumListViewModel(app) as T
                }
                throw IllegalArgumentException("Unable to construct viewmodel")
            }
        }

}