package com.example.spotifyapi.app.domain.usecase

import com.example.spotifyapi.app.data.local.ArtistDB
import com.example.spotifyapi.app.data.repository.TopArtistsRepository
import com.example.spotifyapi.utils.Constants.MEDIUM_TERM

class GetTopArtistsUseCase(
    private val repository: TopArtistsRepository
) {

    suspend fun fetchAndSaveTopArtists(
        offset: Int = 0, timeRange: String = MEDIUM_TERM
    ): List<ArtistDB> {
        val responseApi = repository.getTopArtistsApi(offset, timeRange)
        repository.insertArtists(responseApi)
        return responseApi
    }

    suspend fun preloadAllTopArtists(timeRange: String = MEDIUM_TERM) {
        var offset = 0
        val pageSize = 50
        while (true) {
            val response = repository.getTopArtistsApi(offset, timeRange)
            if (response.isEmpty()) break
            repository.insertArtists(response)
            offset += pageSize
        }
    }
}