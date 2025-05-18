package com.example.spotifyapi.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProfileDB::class,
        PlaylistDB::class
   ],
    version = 2
)
abstract class SpotifyDatabase : RoomDatabase() {

    abstract fun spotifyDao(): SpotifyDAO

    companion object {
        @Volatile private var INSTANCE: SpotifyDatabase? = null

        fun getDatabase(context: Context): SpotifyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpotifyDatabase::class.java,
                    "spotify_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}