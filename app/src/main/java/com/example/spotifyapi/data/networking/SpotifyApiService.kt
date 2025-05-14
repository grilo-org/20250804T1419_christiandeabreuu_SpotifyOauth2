package com.example.spotifyapi.data.networking

import com.example.spotifyapi.data.model.UserProfileResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface SpotifyApiService {

    @GET("me")
    suspend fun getUserProfile(@Header("Authorization") authorization: String): UserProfileResponse.UserProfile

//    @GET("me/top/artists")
//    suspend fun getTopArtists(
//        @Header("Authorization") accessToken: String,
//        @Query("limit") limit: Int = 10,
//        @Query("time_range") timeRange: String = "short_term",
//        @Query("offset") offset: Int = 0
//    ): TopArtistsResponse
//
//    @GET("artists/{id}/albums")
//    fun getAlbumsArtists(@Header("Authorization") authorization: String, @Path("id") artistId: String): Call<AlbumsResponse>
//
//    @GET("me/playlists")
//    suspend fun getPlaylists(@Header("Authorization") authorization: String): PlaylistsResponse
//
//    @POST("me/playlists")
//    suspend fun createPlaylist(
//        @Header("Authorization") accessToken: String,
//        @Body requestBody: CreatePlaylistRequest
//    ): Response<Unit>


}


