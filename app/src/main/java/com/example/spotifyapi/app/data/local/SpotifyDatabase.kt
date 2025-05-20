package com.example.spotifyapi.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

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