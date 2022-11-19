package com.example.vinyls

import android.app.Application
import com.example.vinyls.database.VinylRoomDatabase

class VinylsApplication: Application()  {
    val database by lazy { VinylRoomDatabase.getDatabase(this) }
}