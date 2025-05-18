package com.example.spotifyapi.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SpotifyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalTopArtistsInfo(artists: List<ArtistDB>)

//    @Query("SELECT * FROM artist")
//    suspend fun getLocalTopArtistsInfo(): List<ArtistDB>



        @Query("SELECT * FROM artist")
        suspend fun getLocalTopArtistsInfo(): List<ArtistDB>

        @Query("SELECT * FROM image_artist WHERE artistId = :artistId")
        suspend fun getLocalImagesForArtist(artistId: String): List<ImageArtistDB>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalTopArtistsImages(images: List<ImageArtistDB>)

    @Query("SELECT * FROM image_artist")
    suspend fun getLocalTopArtistImages(): List<ImageArtistDB>






    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalUserProfile(userProfile: UserProfileDB): Long

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getLocalUserProfile(): UserProfileDB?



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalPlaylists(playlists: List<PlaylistDB>)

    @Query("SELECT * FROM playlist")
    suspend fun getLocalPlaylists(): List<PlaylistDB>

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAlbums(albums: List<AlbumDB>)
//
//    @Query("SELECT * FROM albums")
//    suspend fun getAlbums(): List<AlbumDB>
//
//    @Query("SELECT * FROM albums WHERE databaseId = :albumId LIMIT 1")
//    suspend fun getAlbumById(albumId: String): AlbumDB?
}