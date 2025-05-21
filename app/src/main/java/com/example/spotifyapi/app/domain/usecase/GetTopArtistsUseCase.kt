package com.example.spotifyapi.app.domain.usecase

import android.util.Log
import com.example.spotify.data.local.TopArtistsWithArtistsAndImages
import com.example.spotifyapi.app.data.local.SpotifyDAO
import com.example.spotifyapi.app.data.networking.SpotifyApiService
import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.local.ImageArtist
import com.example.spotifyapi.app.data.local.TopArtistsDB
import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import com.example.spotifyapi.app.domain.mapper.TopArtistsMapper

class GetTopArtistsUseCase(
    private val spotifyDAO: SpotifyDAO,
    private val apiService: SpotifyApiService,
    private val repository: TopArtistsRepository = TopArtistsRepository(apiService, spotifyDAO),
) {

    suspend fun getFromApi(
        accessToken: String,
        offset: Int = 0,
        timeRange: String = "medium_term"
    ): TopArtistsResponse {
        Log.d(
            "GetTopArtistsUseCase",
            "Chamada API com: accessToken=Bearer $accessToken, offset=$offset, timeRange=$timeRange"
        )
        val responseApi = repository.getTopArtistsApi(accessToken, offset, timeRange)
        saveToDatabase(responseApi, timeRange)
        return responseApi
    }

    private suspend fun saveToDatabase(response: TopArtistsResponse, timeRange: String) {
        val topArtistsDB = TopArtistsMapper.toTopArtistsDB(response, timeRange)
        val topArtistsId = repository.insertTopArtistsDB(topArtistsDB).toInt()

        val artistsDB = TopArtistsMapper.toArtistsDB(response, topArtistsId)
        val artistsIds = repository.insertArtists(artistsDB)

        val imageArtistsDB = TopArtistsMapper.toImageArtistsDB(response, artistsIds)
        repository.insertImageArtists(imageArtistsDB)
    }

    suspend fun getFromDBWithOffsetAndLimit(
        limit: Int,
        offset: Int,
        timeRange: String = "medium_term"
    ): TopArtistsWithArtistsAndImages {
        val dbResponse = spotifyDAO.getTopArtistsWithOffsetAndLimit(limit, offset, timeRange)
        Log.d(
            "GetTopArtistsUseCase",
            "Artista: ${dbResponse.artists.map { it.artist.name }}, Imagens: ${dbResponse.artists.map { it.images.size }}"
        )
        return dbResponse
    }
}