package com.example.spotifyapi.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spotifyapi.app.data.local.AlbumDB
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.local.PlaylistDB
import com.example.spotifyapi.app.data.local.TopArtistsDB
import com.example.spotifyapi.app.data.local.UserProfileDB

@Database(
    entities = [
        TopArtistsDB::class,
        ArtistDB::class,
        ImageArtist::class,
        UserProfileDB::class,
        PlaylistDB::class,
        AlbumDB::class,
    ],

    version = 8
)
abstract class SpotifyDatabase : RoomDatabase() {

    abstract fun spotifyDao(): SpotifyDAO

}