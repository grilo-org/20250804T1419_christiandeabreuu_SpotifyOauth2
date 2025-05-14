package com.example.spotifyapi.data.networking

import com.example.spotifyapi.data.model.TopArtistsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface SpotifyApiService {

    @GET("me/top/artists")
    fun getTopArtists(
        @Header("Authorization") accessToken: String,
        @Query("limit") limit: Int = 20,
        @Query("time_range") timeRange: String = "medium_term"
    ): Call<TopArtistsResponse>
}


