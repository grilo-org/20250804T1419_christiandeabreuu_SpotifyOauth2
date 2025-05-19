package com.example.spotifyapi.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserProfileDB::class,
        PlaylistDB::class,
        ArtistDB::class,
        ImageArtistDB::class,
        AlbumDB::class],
    version = 7
)
abstract class SpotifyDatabase : RoomDatabase() {

    abstract fun spotifyDao(): SpotifyDAO

}