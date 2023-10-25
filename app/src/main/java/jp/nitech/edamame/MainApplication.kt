package jp.nitech.edamame

import android.app.Application
import androidx.room.Room

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            EdamameDatabase::class.java,
            "edamameDatabase"
        ).build()
    }

    companion object {
        lateinit var database : EdamameDatabase
    }
}