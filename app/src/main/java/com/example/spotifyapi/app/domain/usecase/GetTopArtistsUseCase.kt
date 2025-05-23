package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.model.TopArtistsResponse
import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import com.example.spotifyapi.app.domain.mapper.TopArtistsMapper
import com.example.spotifyapi.utils.Constants.MEDIUM_TERM

class GetTopArtistsUseCase(
    private val repository: TopArtistsRepository
) {

    suspend fun getFromApi(
        offset: Int = 0, timeRange: String = MEDIUM_TERM
    ): TopArtistsResponse {
        val responseApi = repository.getTopArtistsApi(offset, timeRange)
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
}