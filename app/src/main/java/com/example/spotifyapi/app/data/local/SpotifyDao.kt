package com.example.spotifyapi.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.spotifyapi.app.ui.topartists.b.ArtistDB
import com.example.spotify.data.local.ArtistWithImages
import com.example.spotifyapi.app.ui.topartists.b.ImageArtist
import com.example.spotifyapi.app.ui.topartists.b.TopArtistsDB
import com.example.spotify.data.local.TopArtistsWithArtistsAndImages

@Dao
interface SpotifyDAO {

    //iguais
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopArtistsDB(topArtistsDB: TopArtistsDB): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtists(artists: List<ArtistDB>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageArtists(imageArtists: List<ImageArtist>)

    @Transaction
    @Query("SELECT * FROM top_artists WHERE timeRange = :timeRange LIMIT :limit OFFSET :offset")
    suspend fun getTopArtistsWithOffsetAndLimit(
        limit: Int, offset: Int, timeRange: String
    ): TopArtistsWithArtistsAndImages

    @Transaction
    @Query("SELECT * FROM artist WHERE topArtistsId = :topArtistsId LIMIT :limit OFFSET :offset")
    fun getTopArtistsWithImages(topArtistsId: Int, limit: Int, offset: Int): List<ArtistWithImages>








//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertLocalTopArtistsInfo(artists: List<ArtistDB>)
//
//    @Query("SELECT * FROM artist")
//    suspend fun getLocalTopArtistsInfo(): List<ArtistDB>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertLocalTopArtistsImages(images: List<ImageArtistDB>)
//
//    @Query("SELECT * FROM image_artist")
//    suspend fun getLocalTopArtistImages(): List<ImageArtistDB>

    @Query("SELECT * FROM albums WHERE artistId = :artistId")
    suspend fun getLocalAlbumsByArtist(artistId: String): List<AlbumDB>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalAlbums(albums: List<AlbumDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalUserProfile(userProfile: UserProfileDB): Long

    @Query("SELECT * FROM user_profile LIMIT 1")
    suspend fun getLocalUserProfile(): UserProfileDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalPlaylists(playlists: List<PlaylistDB>)

    @Query("SELECT * FROM playlist")
    suspend fun getLocalPlaylists(): List<PlaylistDB>







}