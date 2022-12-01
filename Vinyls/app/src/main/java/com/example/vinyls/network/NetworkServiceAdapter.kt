package com.example.vinyls.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.vinyls.models.Album
import com.example.vinyls.models.Collector
import com.example.vinyls.models.Musician
import com.example.vinyls.models.Track
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NetworkServiceAdapter constructor(context: Context) {
    companion object{
        const val BASE_URL= "https://team7-vynils-back.herokuapp.com/"
        var instance: NetworkServiceAdapter? = null
        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: NetworkServiceAdapter(context).also {
                    instance = it
                }
            }
    }
    private val requestQueue: RequestQueue by lazy {
        // applicationContext keeps you from leaking the Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    suspend fun createAlbum(body: JSONObject) = suspendCoroutine<Album>{ cont->
        Log.d("Crear Album",body.toString())
        requestQueue.add(postRequest("albums", body,
            { response ->
                Log.d("Crear Album", "Album Creado")
                val album=Album(albumId = response.getInt("id"),name = response.getString("name"), cover = response.getString("cover"), recordLabel = response.getString("recordLabel"), releaseDate = response.getString("releaseDate"), genre = response.getString("genre"), description = response.getString("description"))
                cont.resume(album)
            },
            {
                Log.d("Crear Album", "ERROR")
                cont.resumeWithException(it)
            }))
    }

    suspend fun getAlbums() = suspendCoroutine<List<Album>>{ cont ->
        Log.d("Prueba Corutina","Antes de Encolar Obtener Album")
        requestQueue.add(getRequest("albums",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Album>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    list.add(i, Album(albumId = item.getInt("id"),
                        name = item.getString("name"),
                        cover = item.getString("cover"),
                        recordLabel = item.getString("recordLabel"),
                        releaseDate = item.getString("releaseDate"),
                        genre = item.getString("genre"),
                        description = item.getString("description")))
                }
                Log.d("Prueba Corutina","Antes de Devolver Albums")
                cont.resume(list)
            },
            Response.ErrorListener {
                cont.resumeWithException(it)
            }))
    }

    fun getAlbumById(albumId:Int, onComplete:(resp:Album)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(getRequest("albums",
            Response.Listener<String> { response ->
                val resp = JSONObject(response)
                val album = Album(albumId = resp.getInt("id"),name = resp.getString("name"), cover = resp.getString("cover"), recordLabel = resp.getString("recordLabel"), releaseDate = resp.getString("releaseDate"), genre = resp.getString("genre"), description = resp.getString("description"))
                onComplete(album)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }

    suspend fun getMusicians() = suspendCoroutine<List<Musician>>{ cont ->
        requestQueue.add(getRequest("musicians",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Musician>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    list.add(i, Musician(
                        musicianId = item.getInt("id"),
                        name = item.getString("name"),
                        image = item.getString("image"),
                        description = item.getString("description"),
                        birthDate = item.getString("birthDate")))
                }
                cont.resume(list)
            },
            Response.ErrorListener {
                cont.resumeWithException(it)
            }))
    }


    suspend fun getCollectors() = suspendCoroutine<List<Collector>>{ cont ->
        requestQueue.add(getRequest("collectors",
            { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Collector>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    list.add(i, Collector(
                        collectorId = item.getInt("id"),
                        name = item.getString("name"),
                        telephone = item.getString("telephone"),
                        email = item.getString("email")))
                }
                cont.resume(list)
            },
            {
                cont.resumeWithException(it)
            }))
    }

    fun getMusicianById(musicianId:Int, onComplete:(resp:Musician)->Unit, onError: (error:VolleyError)->Unit){
        requestQueue.add(getRequest("musician",
            Response.Listener<String> { response ->
                val resp = JSONObject(response)
                val band = Musician(musicianId = resp.getInt("id"),name = resp.getString("name"), image = resp.getString("image"), description = resp.getString("description"), birthDate = resp.getString("birthDate"))
                onComplete(band)
            },
            Response.ErrorListener {
                onError(it)
            }))
    }

    suspend fun getTracksByAlbumId(albumId:Int) = suspendCoroutine<List<Track>>{ cont ->
        requestQueue.add(getRequest("albums/"+albumId.toString()+"/tracks",
            Response.Listener<String> { response ->
                val resp = JSONArray(response)
                val list = mutableListOf<Track>()
                var item:JSONObject? = null
                for (i in 0 until resp.length()) {
                    item = resp.getJSONObject(i)
                    list.add(i, Track(trackId = item.getInt("id"),
                        name = item.getString("name"),
                        duration = item.getString("duration"))
                    )
                }
                cont.resume(list)
            },
            Response.ErrorListener {
                cont.resumeWithException(it)
            }))
    }

    suspend fun createTrack(body: JSONObject, albumId: Int) = suspendCoroutine<Track>{ cont->
        Log.d("Crear Track",body.toString())
        requestQueue.add(postRequest("albums/"+albumId.toString()+"/tracks", body,
            { response ->
                Log.d("Crear Track", "Track Creado")
                val track=Track(trackId = response.getInt("id"),name = response.getString("name"), duration = response.getString("duration"))
                cont.resume(track)
            },
            {
                Log.d("Crear Track", "ERROR")
                cont.resumeWithException(it)
            }))
    }


    private fun getRequest(path:String, responseListener: Response.Listener<String>, errorListener: Response.ErrorListener): StringRequest {
        return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
    }
    private fun postRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.POST, BASE_URL+path, body, responseListener, errorListener)
    }
    private fun putRequest(path: String, body: JSONObject,  responseListener: Response.Listener<JSONObject>, errorListener: Response.ErrorListener ):JsonObjectRequest{
        return  JsonObjectRequest(Request.Method.PUT, BASE_URL+path, body, responseListener, errorListener)
    }
}