package com.example.spotifyapi.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProfileDB::class,
        PlaylistDB::class,
        ArtistDB::class,
        ImageArtistDB::class
   ],
    version = 5
)
abstract class SpotifyDatabase : RoomDatabase() {

    abstract fun spotifyDao(): SpotifyDAO

}