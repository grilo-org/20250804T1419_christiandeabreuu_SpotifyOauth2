package com.example.spotifyapi.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.spotifyapi.app.ui.topartists.b.TopArtistsDB
import com.example.spotifyapi.app.ui.topartists.b.ArtistDB
import com.example.spotifyapi.app.ui.topartists.b.ImageArtist

@Database(
    entities = [
        TopArtistsDB::class,
        ArtistDB::class,
        ImageArtist::class,
        UserProfileDB::class,
        PlaylistDB::class,
        AlbumDB::class,
//        ImageArtistDB::class,
    ],

    version = 8
    //mudar versao
)
abstract class SpotifyDatabase : RoomDatabase() {

    abstract fun spotifyDao(): SpotifyDAO

}