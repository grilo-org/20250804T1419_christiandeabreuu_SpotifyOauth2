package com.example.spotifyapi.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.local.UserProfileDB

@Database(
    entities = [
        ArtistDB::class,
        UserProfileDB::class,
        PlaylistDB::class,
        AlbumDB::class,
    ],

    version = 9
)
abstract class SpotifyDatabase : RoomDatabase() {

    abstract fun spotifyDao(): SpotifyDAO

}